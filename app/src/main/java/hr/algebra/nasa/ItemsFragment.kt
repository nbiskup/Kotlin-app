package hr.algebra.nasa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.nasa.adapter.ItemAdapter
import hr.algebra.nasa.databinding.FragmentItemsBinding
import hr.algebra.nasa.framework.fetchItems
import hr.algebra.nasa.model.Item

/**
 * A simple [Fragment] subclass.
 * Use the [ItemsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemsFragment : Fragment() {
    private lateinit var binding: FragmentItemsBinding
    private lateinit var items: MutableList<Item>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        items = requireContext().fetchItems()
        binding = FragmentItemsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ItemAdapter(requireContext(), items)
        }
    }

}