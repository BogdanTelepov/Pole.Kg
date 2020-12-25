package ru.app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.app.ui.main.*

class RequestsViewPagerAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                NewRequestsFragment()
            }
            1 -> {
                ConfirmedRequestsFragment()
            }
            else -> ClosedRequestsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        super.getPageTitle(position)
        return when(position) {
            0 -> {
                "Новые"
            }
            1 -> {
                "Подтвержденные"
            }
            else -> {
                "Отмененные"
            }

        }
    }

    override fun getCount(): Int {
        return 3
    }
}