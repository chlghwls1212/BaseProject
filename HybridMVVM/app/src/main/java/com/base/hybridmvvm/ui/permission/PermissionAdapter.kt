package com.base.hybridmvvm.ui.permission

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.base.hybridmvvm.R
import com.base.hybridmvvm.data.model.PermissionItem
import com.base.hybridmvvm.utils.PermissionUtils

class PermissionsAdapter(
    private val activity: Activity,
    private val permissionsList: List<PermissionItem>,
    private val viewModel: PermissionViewModel
) : BaseAdapter() {

    override fun getCount(): Int {
        return permissionsList.size
    }

    override fun getItem(position: Int): Any {
        return permissionsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_permission_list, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val permissionItem = permissionsList[position]
        viewHolder.permissionName.text = permissionItem.name
        viewHolder.permissionDescription.text = permissionItem.description

        viewHolder.permissionButton.setOnClickListener {
            viewModel.requestPermission(activity,permissionItem.permissions)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val permissionName: TextView = view.findViewById(R.id.permissionName)
        val permissionDescription: TextView = view.findViewById(R.id.permissionDescription)
        val permissionButton: Button = view.findViewById(R.id.permissionButton)
    }
}
