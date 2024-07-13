package com.example.tvshowapplication.adapter;
 
import java.util.List;
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.tvshowapplication.utils.TreeUtil;
import com.example.tvshowapplication.vo.NodeVo;

 
/**
 * tree适配器
 *
 * @param <T>
 */
public abstract class TreeListViewAdapter<T> extends BaseAdapter {
 
    protected Context mContext;
    /**
     * 存储所有可见的NodeVo
     */
    protected List<NodeVo> mNodeVos;
    protected LayoutInflater mInflater;
    /**
     * 存储所有的NodeVo
     */
    protected List<NodeVo> mAllNodeVos;
 
    /**
     * 点击的回调接口
     */
    private OnTreeNodeClickListener onTreeNodeVoClickListener;
 
    public interface OnTreeNodeClickListener {
        /**
         * 处理NodeVo click事件
         *
         * @param Node 节点对象
         * @param position 位置
         */
        void onClick(NodeVo Node, int position);
 
    }
 
    public void setOnTreeNodeClickListener(
            OnTreeNodeClickListener onTreeNodeVoClickListener) {
        this.onTreeNodeVoClickListener = onTreeNodeVoClickListener;
    }
 
    /**
     * @param mTree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public TreeListViewAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel, boolean isHide)
            throws IllegalArgumentException, IllegalAccessException {
        mContext = context;
        /**
         * 对所有的Node进行排序
         */
        mAllNodeVos = TreeUtil
                .getSortedNodeVos(datas, defaultExpandLevel, isHide);
        /**
         * 过滤出可见的Node
         */
        mNodeVos = TreeUtil.filterVisibleNodeVo(mAllNodeVos);
        mInflater = LayoutInflater.from(context);
 
        /**
         * 设置节点点击时，可以展开以及关闭；并且将ItemClick事件继续往外公布
         */
        mTree.setOnItemClickListener((parent, view, position, id) -> {
            expandOrCollapse(position);
 
            if (onTreeNodeVoClickListener != null) {
                onTreeNodeVoClickListener.onClick(mNodeVos.get(position), position);
            }
        });
 
    }
 
    /**
     * 相应ListView的点击事件 展开或关闭某节点
     *
     * @param position 位置
     */
    public void expandOrCollapse(int position) {
        NodeVo n = mNodeVos.get(position);
 
        if (n != null)// 排除传入参数错误异常
        {
            if (!n.isLeaf()) {
                n.setExpand(!n.isExpand());
                mNodeVos = TreeUtil.filterVisibleNodeVo(mAllNodeVos);
                notifyDataSetChanged();// 刷新视图
            }
        }
    }
 
    @Override
    public int getCount() {
        return mNodeVos.size();
    }
 
    @Override
    public Object getItem(int position) {
        return mNodeVos.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final NodeVo NodeVo = mNodeVos.get(position);
 
        convertView = getConvertView(NodeVo, position, convertView, parent);
        // 设置内边距
        convertView.setPadding(NodeVo.getLevel() * 30, 3, 3, 3);
        if (!NodeVo.isHideChecked()) {
            //获取各个节点所在的父布局
            RelativeLayout myView = (RelativeLayout) convertView;
            //父布局下的CheckBox
            myView.getChildAt(1);
        }
 
        return convertView;
    }
 
    public abstract View getConvertView(NodeVo NodeVo, int position, View convertView, ViewGroup parent);
 
}