package com.example.tvshowapplication.utils;
 
import com.example.tvshowapplication.R;
import com.example.tvshowapplication.vo.NodeVo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
 

 
public class TreeUtil {
 
    /**
     * 根据所有节点获取可见节点
     *
     * @param allNodeVos
     * @return
     */
    public static List<NodeVo> filterVisibleNodeVo(List<NodeVo> allNodeVos) {
        List<NodeVo> visibleNodeVos = new ArrayList<NodeVo>();
        for (NodeVo NodeVo : allNodeVos) {
            // 如果为根节点，或者上层目录为展开状态
            if (NodeVo.isRoot() || NodeVo.isParentExpand()) {
                setNodeVoIcon(NodeVo);
                visibleNodeVos.add(NodeVo);
            }
        }
        return visibleNodeVos;
    }
 
    /**
     * 获取排序的所有节点
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<NodeVo> getSortedNodeVos(List<T> datas,
                                                int defaultExpandLevel, boolean isHide)
            throws IllegalAccessException, IllegalArgumentException {
        List<NodeVo> sortedNodeVos = new ArrayList<NodeVo>();
        // 将用户数据转化为List<NodeVo>
        List<NodeVo> NodeVos = convertData2NodeVos(datas, isHide);
        // 拿到根节点
        List<NodeVo> rootNodeVos = getRootNodeVos(NodeVos);
        // 排序以及设置NodeVo间关系
        for (NodeVo NodeVo : rootNodeVos) {
            addNodeVo(sortedNodeVos, NodeVo, defaultExpandLevel, 1);
        }
        return sortedNodeVos;
    }
 
    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addNodeVo(List<NodeVo> NodeVos, NodeVo NodeVo,
                                int defaultExpandLeval, int currentLevel) {
 
        NodeVos.add(NodeVo);
        if (defaultExpandLeval >= currentLevel) {
            NodeVo.setExpand(true);
        }
 
        if (NodeVo.isLeaf())
            return;
        for (int i = 0; i < NodeVo.getChildrenNodeVos().size(); i++) {
            addNodeVo(NodeVos, NodeVo.getChildrenNodeVos().get(i), defaultExpandLeval,
                    currentLevel + 1);
        }
    }
 
    /**
     * 获取所有的根节点
     *
     * @param NodeVos
     * @return
     */
    public static List<NodeVo> getRootNodeVos(List<NodeVo> NodeVos) {
        List<NodeVo> rootNodeVos = new ArrayList<NodeVo>();
        for (NodeVo NodeVo : NodeVos) {
            if (NodeVo.isRoot()) {
                rootNodeVos.add(NodeVo);
            }
        }
 
        return rootNodeVos;
    }
 
    /**
     * 将泛型datas转换为NodeVo
     *
     * @param datas
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<NodeVo> convertData2NodeVos(List<T> datas, boolean isHide)
            throws IllegalAccessException, IllegalArgumentException {
        List<NodeVo> NodeVos = new ArrayList<NodeVo>();
        NodeVo NodeVo = null;
 
        for (T t : datas) {
            int id = -1;
            int pId = -1;
            String name = null;
 
            Class<? extends Object> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            /**
             * 与MyNodeVoBean实体一一对应
             */
            for (Field f : declaredFields) {
                if ("id".equals(f.getName())) {
                    f.setAccessible(true);
                    id = f.getInt(t);
                }
 
                if ("pId".equals(f.getName())) {
                    f.setAccessible(true);
                    pId = f.getInt(t);
                }
 
                if ("name".equals(f.getName())) {
                    f.setAccessible(true);
                    name = (String) f.get(t);
                }
 
                if ("desc".equals(f.getName())) {
                    continue;
                }
 
                if ("length".equals(f.getName())) {
                    continue;
                }
 
                if (id == -1 && pId == -1 && name == null) {
                    break;
                }
            }
 
            NodeVo = new NodeVo(id, pId, name);
            NodeVo.setHideChecked(isHide);
            NodeVos.add(NodeVo);
        }
 
        /**
         * 比较NodeVos中的所有节点，分别添加children和parent
         */
        for (int i = 0; i < NodeVos.size(); i++) {
            NodeVo n = NodeVos.get(i);
            for (int j = i + 1; j < NodeVos.size(); j++) {
                NodeVo m = NodeVos.get(j);
                if (n.getId() == m.getpId()) {
                    n.getChildrenNodeVos().add(m);
                    m.setParent(n);
                } else if (n.getpId() == m.getId()) {
                    n.setParent(m);
                    m.getChildrenNodeVos().add(n);
                }
            }
        }
 
        for (NodeVo n : NodeVos) {
            setNodeVoIcon(n);
        }
        return NodeVos;
    }
 
    /**
     * 设置打开，关闭icon
     *
     * @param NodeVo
     */
    public static void setNodeVoIcon(NodeVo NodeVo) {
        if (NodeVo.getChildrenNodeVos().size() > 0 && NodeVo.isExpand()) {
            NodeVo.setIcon(R.drawable.tree_expand);
        } else if (NodeVo.getChildrenNodeVos().size() > 0 && !NodeVo.isExpand()) {
            NodeVo.setIcon(R.drawable.tree_econpand);
        } else
            NodeVo.setIcon(-1);
    }
 
    public static void setNodeVoChecked(NodeVo NodeVo, boolean isChecked) {
        // 自己设置是否选择
        NodeVo.setChecked(isChecked);
        /**
         * 非叶子节点,子节点处理
         */
        setChildrenNodeVoChecked(NodeVo, isChecked);
        /** 父节点处理 */
        setParentNodeVoChecked(NodeVo);
 
    }
 
    /**
     * 非叶子节点,子节点处理
     */
    private static void setChildrenNodeVoChecked(NodeVo NodeVo, boolean isChecked) {
        NodeVo.setChecked(isChecked);
        if (!NodeVo.isLeaf()) {
            for (NodeVo n : NodeVo.getChildrenNodeVos()) {
                // 所有子节点设置是否选择
                setChildrenNodeVoChecked(n, isChecked);
            }
        }
    }
 
    /**
     * 设置父节点选择
     *
     * @param NodeVo
     */
    private static void setParentNodeVoChecked(NodeVo NodeVo) {
 
        /** 非根节点 */
        if (!NodeVo.isRoot()) {
            NodeVo rootNodeVo = NodeVo.getParent();
            boolean isAllChecked = true;
            for (NodeVo n : rootNodeVo.getChildrenNodeVos()) {
                if (!n.isChecked()) {
                    isAllChecked = false;
                    break;
                }
            }
 
            if (isAllChecked) {
                rootNodeVo.setChecked(true);
            } else {
                rootNodeVo.setChecked(false);
            }
            setParentNodeVoChecked(rootNodeVo);
        }
    }
 
}