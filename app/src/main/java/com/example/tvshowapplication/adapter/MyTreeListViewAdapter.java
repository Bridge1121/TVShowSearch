package com.example.tvshowapplication.adapter;
 
import java.util.List;
 
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tvshowapplication.R;
import com.example.tvshowapplication.vo.NodeVo;

 
public class MyTreeListViewAdapter<T> extends TreeListViewAdapter<T> {
 
    public MyTreeListViewAdapter(ListView mTree, Context context,
                                 List<T> datas, int defaultExpandLevel, boolean isHide)
            throws IllegalArgumentException, IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel, isHide);
    }
 
    @Override
    public View getConvertView(NodeVo node, int position, View convertView,
                               ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tree_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = convertView.findViewById(R.id.id_treenode_icon);
            viewHolder.label = convertView.findViewById(R.id.id_treenode_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }
 
        viewHolder.label.setText(node.getName());
        return convertView;
    }
 
    private static final class ViewHolder {
        ImageView icon;
        TextView label;
    }
}