package com.brandsin.store.ui.main.categories.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentCategoriesBinding
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.categories.CategoriesAdapter
import com.brandsin.store.ui.main.categories.CategoriesViewModel
import com.brandsin.store.ui.main.categories.model.CategoryItem

class CategoriesFragment : BaseHomeFragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoriesViewModel by viewModels()

    private lateinit var categoriesAdapter: CategoriesAdapter

    private val btnDeleteClickCallBack: (category: CategoryItem) -> Unit = { category ->
        viewModel.deleteCategoryByCategoryId(categoryId = category.id ?: 0)
    }

    private val btnEditClickCallBack: (category: CategoryItem) -> Unit = { category ->
        val dialogFragment = EditCategoryDialogFragment.newInstance(
            type = "edit_category",
            categoryId = category.id,
            nameAr = category.name,
            nameEn = category.nameEn
        ) {
            viewModel.getAllCategories()
        }
        dialogFragment.show(requireActivity().supportFragmentManager, "CustomDialogFragmentTag")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.categories))

        viewModel.getAllCategories()

        initRecycler()
        setBtnListener()
        subscribeData()
    }

    private fun setBtnListener() {
        binding.root.setOnRefreshListener {
            viewModel.getAllCategories()
            binding.root.isRefreshing = false
        }

        binding.btnAddNewCategory.setOnClickListener {
            val dialogFragment = EditCategoryDialogFragment.newInstance(
                type = "add_category",
                categoryId = 0,
                nameAr = null,
                nameEn = null
            ) { viewModel.getAllCategories() }
            dialogFragment.show(childFragmentManager, "CustomDialogFragmentTag")
        }
    }

    private fun initRecycler() {
        binding.rvCategories.apply {
            categoriesAdapter = CategoriesAdapter(btnDeleteClickCallBack, btnEditClickCallBack)
            adapter = categoriesAdapter
        }
    }

    private fun subscribeData() {
        viewModel.getAllCategoriesResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    categoriesAdapter.submitData(it.data?.categoriesList)
                }

                is ResponseHandler.Error -> {
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)

                    // show error message
                    showToast(it.message, 2)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(true)
                    viewModel.obsIsEmpty.set(false)
                    viewModel.obsIsFull.set(false)
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(false)
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)
                }

                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}