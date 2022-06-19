package com.jihad.aitools.feature_extracttext.presentation.extract_text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.mlkit.vision.common.internal.ImageUtils;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.FragmentExtractTextBinding;
import com.jihad.aitools.feature_extracttext.Core;
import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;
import com.jihad.aitools.feature_extracttext.presentation.history.ExtractTextHistoryViewModel;

import java.io.IOException;
import java.util.List;

public class ExtractTextFragment extends Fragment {

    private FragmentExtractTextBinding binding;
    private ClipboardManager clipboardManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_extract_text, container);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentExtractTextBinding.bind(view);

        binding.laCopy.setEnabled(false);

        /*                       Listeners                   */
        binding.ml.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
                if (motionLayout.getProgress() >= 0.6){ // if the transition is approaching to end
                    binding.et.setSingleLine(false);
                }else
                    binding.et.setSingleLine(true);
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });

        binding.laTempImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        binding.laCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("",binding.et.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(requireContext(), getString(R.string.TextCopied), Toast.LENGTH_SHORT).show();
            }
        });

/*                            Observables                      */

        final Observer<String> extractedTextObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.laCopy.setEnabled(true);
                binding.laCopy.playAnimation();
                binding.et.setText(s);

                //  Here we are making sure that when the user have chose an image, then the entity will be added
                //  not if the user clicked on one of the entities history
                if (Core.extractTextViewModel.chosenImage.getValue() != null)
                    Core.extractTextViewModel.addEntity(binding.et.getText().toString(), Core.extractTextViewModel.chosenImage.getValue());
            }
        };
        Core.extractTextViewModel.extractedText.observe(requireActivity(), extractedTextObserver);

        final Observer<Bitmap> chosenImageObserver = new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binding.laTempImage.setImageBitmap(bitmap);
            }
        };
        Core.extractTextViewModel.chosenImage.observe(requireActivity(), chosenImageObserver);

    } // end of on viewCreated

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
              Handler handler = new Handler(Looper.getMainLooper());
              handler.post(new Runnable() {
                  @Override
                  public void run() {
                      if (result != null) { // if the user actually chose an image
                          try {
                             Bitmap bm = ImageUtils.getInstance().zza(requireContext().getContentResolver(), result);
                              Core.extractTextViewModel.setChosenImage(bm, getContext().getContentResolver());
                              Core.extractTextViewModel.extractText(result, requireContext());
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }
                  }
              });
            }
    ); // end of mGetContent

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}