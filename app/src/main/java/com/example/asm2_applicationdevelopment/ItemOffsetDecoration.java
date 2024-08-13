package com.example.asm2_applicationdevelopment;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private final int itemOffset;

    public ItemOffsetDecoration(int itemOffset) {
        this.itemOffset = itemOffset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = itemOffset; // Add space to the bottom of the item
    }
}
