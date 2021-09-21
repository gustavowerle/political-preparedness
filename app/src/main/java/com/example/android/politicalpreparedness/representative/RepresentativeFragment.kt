package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class RepresentativeFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativeBinding
    private val viewModel: RepresentativeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepresentativeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        val adapter = RepresentativeListAdapter()
        binding.recyclerRepresentatives.adapter = adapter
        binding.recyclerRepresentatives.layoutManager = LinearLayoutManager(context)

        viewModel.representative.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.message.observe(viewLifecycleOwner, Observer {
            showMessage(it)
        })

        binding.spinnerState.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.states)
        )

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            viewModel.searchRepresentatives()

        }

        binding.buttonLocation.setOnClickListener {
            if (canAccessLocation()) {
                getLocation()
            }
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            } else {
                showMessage("To use this feature, you need to give location permission")
            }
        }
    }

    private fun canAccessLocation(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val address = geoCodeLocation(location)
                viewModel.address = address

                binding.editTextAddressLine1.setText(address.line1)
                binding.editTextAddressLine2.setText(address.line2)
                binding.editTextCity.setText(address.city)
                binding.editTextZip.setText(address.zip)

                val states = resources.getStringArray(R.array.states)
                val selectedStateIndex = states.indexOf(address.state)
                binding.spinnerState.setSelection(selectedStateIndex)

                viewModel.searchRepresentatives()
            }
        }.addOnFailureListener { e ->
            Log.e(TAG, e.message.toString())
            showMessage("Something went wrong on capturing your location please try again")
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun showMessage(message: String) {
        context?.apply {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1000
        private const val TAG = "RepresentativeFragment"
    }
}
