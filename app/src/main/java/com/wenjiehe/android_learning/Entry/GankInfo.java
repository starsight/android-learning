package com.wenjiehe.android_learning.Entry;

import java.util.List;
import java.util.Map;

/**
 * @author wenjie
 */
public class GankInfo {
    public boolean error;
    public Map<String, List<GankItem>> results;
    public List<String> category;

    public boolean hasData() {
        return results != null && results.size() > 0;
    }
}
