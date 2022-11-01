package com.brandsin.store.ui.main.offers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentOffersBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.offers.delete.DeleteOfferResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.dialogs.confirm.DialogConfirmFragment
import com.brandsin.store.ui.main.addofffer.AddOfferActivity
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.user.model.constants.Params
import timber.log.Timber

class OffersFragment : BaseHomeFragment(), Observer<Any?>
{
    private lateinit var binding : HomeFragmentOffersBinding
    private lateinit var offersViewModel: OffersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_offers, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        offersViewModel = ViewModelProvider(this).get(OffersViewModel::class.java)
        binding.viewModel = offersViewModel

        setBarName(getString(R.string.offers))

        offersViewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        offersViewModel.offersAdapter.editLiveData.observe(viewLifecycleOwner, {
            val intent = Intent(requireActivity(), AddOfferActivity::class.java)
            intent.putExtra(Params.OFFER_ITEM , it)
            startActivityForResult(intent, Codes.UPDATE_OFFER)
        })

        offersViewModel.offersAdapter.deleteLiveData.observe(viewLifecycleOwner, {
            val bundle = Bundle()
            bundle.putString(Params.DIALOG_CONFIRM_MESSAGE, MyApp.getInstance().getString(R.string.delete_cart_warning))
            bundle.putString(Params.DIALOG_CONFIRM_POSITIVE, MyApp.getInstance().getString(R.string.confirm))
            bundle.putString(Params.DIALOG_CONFIRM_NEGATIVE, MyApp.getInstance().getString(R.string.ignore))
            bundle.putSerializable(Params.OFFER_ITEM, it)
            Utils.startDialogActivity(requireActivity(), DialogConfirmFragment::class.java.name, Codes.DIALOG_CONFIRM_REQUEST, bundle)
        })

        observe(offersViewModel.apiResponseLiveData) {
            when (it.status) {
                Status.ERROR_MESSAGE -> {
                    showToast(it.message.toString(), 1)
                }
                Status.SUCCESS_MESSAGE -> {
                    showToast(it.message.toString(), 2)
                }
                Status.SUCCESS -> {
                    when (it.data) {
                        is DeleteOfferResponse -> {
                            offersViewModel.getOffers()
                        }
                    }
                }
                else -> {
                    Timber.e(it.message)
                }
            }
        }

        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            offersViewModel.getUserStatus()
        }
    }

    override fun onChanged(it: Any?) {
        when (it) {
            null -> return
            else -> it.let {
                when (it) {
                    Codes.ADD_OFFER -> {
                        val intent = Intent(requireActivity(), AddOfferActivity::class.java)
                        startActivityForResult(intent, Codes.ADD_OFFER)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == Codes.DIALOG_CONFIRM_REQUEST && data != null -> {
                when {
                    data.hasExtra(Params.DIALOG_CLICK_ACTION) -> {
                        when {
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION,1) == 1 -> {
                                if (data.getIntExtra(Params.OFFER_ITEM,0) != 0) {
                                    offersViewModel.deleteOfferRequest.offerId = data.getIntExtra(Params.OFFER_ITEM,0)
                                    offersViewModel.deleteOffer()
                                }
                            }
                        }
                    }
                }
            }
        }

        when {
            requestCode == Codes.UPDATE_OFFER && data != null -> {
                when {
                    data.hasExtra(Params.DIALOG_CLICK_ACTION) -> {
                        when {
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION,1) == 1 -> {
                                offersViewModel.getOffers()
                            }
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION,1) == 2 -> {
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }

        when {
            requestCode == Codes.ADD_OFFER && data != null -> {
                when {
                    data.hasExtra(Params.DIALOG_CLICK_ACTION) -> {
                        when {
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION,1) == 1 -> {
                                offersViewModel.getOffers()
                            }
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION,1) == 2 -> {
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }

}