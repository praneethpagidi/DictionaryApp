package com.example.dictionary

import androidx.test.rule.ActivityTestRule
import com.example.dictionary.ui.activities.MainActivity

/**
 * Created by PraNeeTh on 4/13/20
 */
class FragmentTestHelper(activityClass: Class<MainActivity>?) :
    ActivityTestRule<MainActivity>(activityClass)