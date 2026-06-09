package com.tenor.android.core.response.impl;

import androidx.annotation.NonNull;

import com.tenor.android.core.model.impl.Tag;
import com.tenor.android.core.response.AbstractResponse;
import com.tenor.android.core.util.AbstractListUtils;

import java.util.Collections;
import java.util.List;

/**
 * The response of v2 categories
 */
public class CategoriesResponse extends AbstractResponse {

    private static final long serialVersionUID = 472621992315821442L;
    private List<Tag> tags;

    @NonNull
    public List<Tag> getTags() {
        return !AbstractListUtils.isEmpty(tags) ? tags : Collections.<Tag>emptyList();
    }
}
