package com.powerje.todo.data.adapters;

/*
 * Copyright (C) 2014. Victor Kosenko (http://qip-blog.eu.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * This is basic implementation of swipable cursor adapter that allows to skip displaying dismissed
 * items by replacing them with empty view. This adapter overrides default implementation of
 * {@link #getView(int, android.view.View, android.view.ViewGroup)}, so if you have custom
 * implementation of this method you should review it according to logic of this adapter.
 *
 * @author Victor Kosenko
 */
public abstract class BaseSwipableCursorAdapter extends CursorAdapter {

    protected static final int VIEW_ITEM_NORMAL = 0;
    protected static final int VIEW_ITEM_EMPTY = 1;

    protected Set<Long> pendingDismissItems;
    protected View emptyView;
    protected LayoutInflater inflater;

    /**
     * If {@code true} all pending items will be removed on cursor swap
     */
    protected boolean flushPendingItemsOnSwap = true;

    /**
     * @see android.widget.CursorAdapter#CursorAdapter(android.content.Context, android.database.Cursor, boolean)
     */
    public BaseSwipableCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        init(context);
    }

    /**
     * @see android.widget.CursorAdapter#CursorAdapter(android.content.Context, android.database.Cursor, int)
     */
    protected BaseSwipableCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        init(context);
    }

    /**
     * Constructor with {@code null} cursor and enabled autoRequery
     *
     * @param context The context
     */
    protected BaseSwipableCursorAdapter(Context context) {
        super(context, null, true);
        init(context);
    }

    /**
     * @param context                 The context
     * @param flushPendingItemsOnSwap If {@code true} all pending items will be removed on cursor swap
     * @see #BaseSwipableCursorAdapter(android.content.Context)
     */
    protected BaseSwipableCursorAdapter(Context context, boolean flushPendingItemsOnSwap) {
        super(context, null, true);
        init(context);
        this.flushPendingItemsOnSwap = flushPendingItemsOnSwap;
    }

    protected void init(Context context) {
        inflater = LayoutInflater.from(context);
        pendingDismissItems = new HashSet<>();
        emptyView = new View(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!getCursor().moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        if (isPendingDismiss(position)) {
            return emptyView;
        } else {
            return super.getView(position, convertView, parent);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return pendingDismissItems.contains(getItemId(position)) ? VIEW_ITEM_EMPTY : VIEW_ITEM_NORMAL;
    }

    /**
     * Add item to pending dismiss. This item will be ignored in
     * {@link #getView(int, android.view.View, android.view.ViewGroup)} when displaying list of items
     *
     * @param id Id of item that needs to be added to pending for dismiss
     * @return {@code true} if this item already in collection if pending items, {@code false} otherwise
     */
    public boolean putPendingDismiss(Long id) {
        return pendingDismissItems.add(id);
    }

    /**
     * Confirm that specified item is no longer present in underlying cursor. This method should be
     * called after the fact of removing this item from result set of underlying cursor.
     * If you're using flushPendingItemsOnSwap flag there is no need to call this method.
     *
     * @param id Id of item
     * @return {@code true} if this item successfully removed from pending to dismiss, {@code false}
     * if it's not present in pending items collection
     */
    public boolean commitDismiss(Long id) {
        return pendingDismissItems.remove(id);
    }

    /**
     * Check if this item should be ignored
     *
     * @param position Cursor position
     * @return {@code true} if this item should be ignored, {@code false} otherwise
     */
    public boolean isPendingDismiss(int position) {
        return getItemViewType(position) == VIEW_ITEM_EMPTY;
    }

    public boolean isFlushPendingItemsOnSwap() {
        return flushPendingItemsOnSwap;
    }

    /**
     * Automatically flush pending items when calling {@link #swapCursor(android.database.Cursor)}
     *
     * @param flushPendingItemsOnSwap If {@code true} all pending items will be removed on cursor swap
     */
    public void setFlushPendingItemsOnSwap(boolean flushPendingItemsOnSwap) {
        this.flushPendingItemsOnSwap = flushPendingItemsOnSwap;
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        if (flushPendingItemsOnSwap) {
            pendingDismissItems.clear();
        }
        return super.swapCursor(newCursor);
    }
}
