package com.jihad.aitools.feature_extracttext;

import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;
import com.jihad.aitools.feature_extracttext.presentation.extract_text.ExtractTextViewModel;
import com.jihad.aitools.feature_extracttext.presentation.history.ExtractTextHistoryAdapter;
import com.jihad.aitools.feature_extracttext.presentation.history.ExtractTextHistoryViewModel;

import java.util.List;

public class CoreET {

    public static ExtractTextViewModel extractTextViewModel;
    public static ExtractTextHistoryViewModel extractTextHistoryViewModel;
    public static List<ExtractTextEntity> list;
    public static ExtractTextHistoryAdapter extractTextHistoryAdapter;
    public static boolean shouldAddEntity;

}
