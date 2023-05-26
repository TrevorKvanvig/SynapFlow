package com.hfad.synapflow

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.firestore.FirebaseFirestore



class Rewards : Fragment() {
    data class RewardItem(val icon: Int, val description: String)
    private val fd = FirestoreData()
    // create list to hold items in Horizontal scroll view
    private val rewardsToEarn = mutableListOf<RewardItem>()
    private val earnedRewards = mutableListOf<RewardItem>()

    // used to keep track if the lists have already been changed for number of completions
    private var didInit = false
    private var didInit1 = false
    private var didInit5 = false
    private var didInit10 = false
    private var didInit15 = false
    private var didInit20 = false
    private var didInit25 = false
    private var didInit30 = false
    private var didInit35 = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Rewards | SynapFlow"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(!didInit){
            initRewards()
            didInit = true
        }
        return inflater.inflate(R.layout.fragment_rewards, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get refrence to both Layouts
        val toEarnRewardsLinearLayout = view.findViewById<LinearLayout>(R.id.clRewardToEarn)
        val earnedRewardsLinearLayout = view.findViewById<LinearLayout>(R.id.clEarnedRewardItem)

        // remove what was created inside of layouts if there is already layouts in from before
        toEarnRewardsLinearLayout.removeAllViews()
        earnedRewardsLinearLayout.removeAllViews()

        // add trophies to screen
        populateRewards(earnedRewardsLinearLayout, earnedRewards)
        populateRewards(toEarnRewardsLinearLayout, rewardsToEarn)

        //place all trophies in to earn section if not done already
        if(!didInit){
            initRewards()
            didInit = true
        }

        // get completion count from firebase
        checkCompletionCount { cc ->
            // if completion count from firebase is 1 or more
            if(cc >= 1){
                // if not done already
                if(!didInit1){
                    // create reward Item to add in completed
                    val rewardItem = RewardItem(R.drawable.trophy, "Completed 1 Study Session")
                    //add created reward in earned rewards list
                    earnedRewards.add(rewardItem)
                    // remove the reward corresponding with the one added to other list
                    rewardsToEarn.removeAt(0)

                    //reset view
                    toEarnRewardsLinearLayout.removeAllViews()
                    earnedRewardsLinearLayout.removeAllViews()
                    populateRewards(earnedRewardsLinearLayout, earnedRewards)
                    populateRewards(toEarnRewardsLinearLayout, rewardsToEarn)
                    // mark done
                    didInit1 = true
                }
            }
            // same functionality as first if statement
            if(cc >= 5){
                if(!didInit5){
                    val rewardItem = RewardItem(R.drawable.trophy1, "Completed 5 Study Session")
                    rewardsToEarn.removeAt(0)
                    earnedRewards.add(rewardItem)
                    toEarnRewardsLinearLayout.removeAllViews()
                    earnedRewardsLinearLayout.removeAllViews()
                    populateRewards(earnedRewardsLinearLayout, earnedRewards)
                    populateRewards(toEarnRewardsLinearLayout, rewardsToEarn)
                    didInit5 = true
                }
            }
            // same functionality as first if statement
            if(cc >= 10){
                if(!didInit10){
                    val rewardItem = RewardItem(R.drawable.trophy2, "Completed 10 Study Session")
                    rewardsToEarn.removeAt(0)
                    earnedRewards.add(rewardItem)
                    toEarnRewardsLinearLayout.removeAllViews()
                    earnedRewardsLinearLayout.removeAllViews()
                    populateRewards(earnedRewardsLinearLayout, earnedRewards)
                    populateRewards(toEarnRewardsLinearLayout, rewardsToEarn)
                    didInit10 = true
                }
            }
            // same functionality as first if statement
            if(cc >= 15){
                if(!didInit15){
                    val rewardItem = RewardItem(R.drawable.trophy3, "Completed 15 Study Session")
                    rewardsToEarn.removeAt(0)
                    earnedRewards.add(rewardItem)
                    toEarnRewardsLinearLayout.removeAllViews()
                    earnedRewardsLinearLayout.removeAllViews()
                    populateRewards(earnedRewardsLinearLayout, earnedRewards)
                    populateRewards(toEarnRewardsLinearLayout, rewardsToEarn)
                    didInit15 = true
                }
            }
            // same functionality as first if statement
            if(cc >= 20){
                if(!didInit20){
                    val rewardItem = RewardItem(R.drawable.trophy4, "Completed 20 Study Session")
                    rewardsToEarn.removeAt(0)
                    earnedRewards.add(rewardItem)
                    toEarnRewardsLinearLayout.removeAllViews()
                    earnedRewardsLinearLayout.removeAllViews()
                    populateRewards(earnedRewardsLinearLayout, earnedRewards)
                    populateRewards(toEarnRewardsLinearLayout, rewardsToEarn)
                    didInit20 = true
                }
            } // same functionality as first if statement
            if(cc >= 25){
                if(!didInit25){
                    val rewardItem = RewardItem(R.drawable.trophy5, "Completed 25 Study Session")
                    rewardsToEarn.removeAt(0)
                    earnedRewards.add(rewardItem)
                    toEarnRewardsLinearLayout.removeAllViews()
                    earnedRewardsLinearLayout.removeAllViews()
                    populateRewards(earnedRewardsLinearLayout, earnedRewards)
                    populateRewards(toEarnRewardsLinearLayout, rewardsToEarn)
                    didInit25 = true
                }
            } // same functionality as first if statement
            if(cc >= 30){
                if(!didInit30){
                    val rewardItem = RewardItem(R.drawable.trophy6, "Completed 30 Study Session")
                    rewardsToEarn.removeAt(0)
                    earnedRewards.add(rewardItem)
                    toEarnRewardsLinearLayout.removeAllViews()
                    earnedRewardsLinearLayout.removeAllViews()
                    populateRewards(earnedRewardsLinearLayout, earnedRewards)
                    populateRewards(toEarnRewardsLinearLayout, rewardsToEarn)
                    didInit30 = true
                }
            } // same functionality as first if statement
            if(cc >= 35){
                if(!didInit35){
                    val rewardItem = RewardItem(R.drawable.trophy7, "Completed 35 Study Session")
                    rewardsToEarn.removeAt(0)
                    earnedRewards.add(rewardItem)
                    toEarnRewardsLinearLayout.removeAllViews()
                    earnedRewardsLinearLayout.removeAllViews()
                    populateRewards(earnedRewardsLinearLayout, earnedRewards)
                    populateRewards(toEarnRewardsLinearLayout, rewardsToEarn)
                    didInit35 = true
                }
            }
        }

    }

    private fun populateRewards(RewardsLL: LinearLayout, rewardsList: List<RewardItem>) {
        //go through every created reward
        for (reward in rewardsList) {
            val displayMetrics = resources.displayMetrics
            val dpToPx = { dp: Int -> TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics).toInt() }

            // Create ImageView to display the reward icon
            val iconImageView = ImageView(requireContext()).apply {
                // set parameters for layout
                layoutParams = ConstraintLayout.LayoutParams(dpToPx(64), dpToPx(53)).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    //bottomToTop = R.id.earnedRewardsDescription
                    horizontalBias = 0.5f
                    verticalBias = 0.5f
                    setMargins(10, dpToPx(15), 0, dpToPx(10))
                }
                adjustViewBounds = false
                cropToPadding = false
                scaleType = ImageView.ScaleType.CENTER_INSIDE

                setImageResource(reward.icon)
            }

            // create text view
            val descriptionTextView = TextView(context).apply {
                // set parameters
                layoutParams = ConstraintLayout.LayoutParams(dpToPx(122), dpToPx(49)).apply {
                    bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    verticalBias = 0.849f
                }
                gravity = Gravity.CENTER
                text = reward.description
            }


            // Add ImageView and TextView to LinearLayout with created parameters
            val rewardLayout = LinearLayout(requireContext())
            rewardLayout.orientation = LinearLayout.VERTICAL
            rewardLayout.gravity = Gravity.CENTER
            rewardLayout.addView(iconImageView)
            rewardLayout.addView(descriptionTextView)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.marginEnd = resources.getDimensionPixelSize(R.dimen.reward_item_margin_end)
            RewardsLL.addView(rewardLayout, layoutParams)
        }

    }

    private fun checkCompletionCount(completion: (Long) -> Unit) {
        // get completion count from firebase asynchronously
        fd.getCompletionCount { completionCount ->
            Log.d("Completion Count", "The current completion count is: $completionCount")
            completion(completionCount)
        }
    }

    private fun initRewards(){
        // create all rewards for initial display when completion count is 0
        val rewardItem = RewardItem(R.drawable.trophy, "Complete 1 Study Session")
        val rewardItem1 = RewardItem(R.drawable.trophy1, "Complete 5 Study Session")
        val rewardItem2 = RewardItem(R.drawable.trophy2, "Complete 10 Study Session")
        val rewardItem3 = RewardItem(R.drawable.trophy3, "Complete 15 Study Session")
        val rewardItem4 = RewardItem(R.drawable.trophy4, "Complete 20 Study Session")
        val rewardItem5 = RewardItem(R.drawable.trophy5, "Complete 25 Study Session")
        val rewardItem6 = RewardItem(R.drawable.trophy6, "Complete 30 Study Session")
        val rewardItem7 = RewardItem(R.drawable.trophy7, "Complete 35 Study Session")
        rewardsToEarn.add(rewardItem)
        rewardsToEarn.add(rewardItem1)
        rewardsToEarn.add(rewardItem2)
        rewardsToEarn.add(rewardItem3)
        rewardsToEarn.add(rewardItem4)
        rewardsToEarn.add(rewardItem5)
        rewardsToEarn.add(rewardItem6)
        rewardsToEarn.add(rewardItem7)
    }
}