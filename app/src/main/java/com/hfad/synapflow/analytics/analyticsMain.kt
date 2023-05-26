package com.hfad.synapflow.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hfad.synapflow.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [analyticsMain.newInstance] factory method to
 * create an instance of this fragment.
 */
public fun launchPlots() {

}

class analyticsMain : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var plots = Figures()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Analytics | Synapflow"
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val frag1 = childFragmentManager.findFragmentById(R.id.fragmentContainerView2)  as stats1
        //val frag2 = childFragmentManager.findFragmentById(R.id.plot2) as statLine
        //frag1.setPlot(plots)
        //frag2.setPlot(plots)
        return inflater.inflate(R.layout.fragment_analytics_main, container, false)
    }

    /*
    open fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    val view: View = inflater.inflate(R.layout.parent_fragment, container, false)
    val fragment: YourChildFragment? =
        childFragmentManager.findFragmentById(R.id.child_fragment_id) as YourChildFragment?

    // pass your data
    fragment.setMyInt(42)
    return view
     */

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment analyticsMain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            analyticsMain().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}