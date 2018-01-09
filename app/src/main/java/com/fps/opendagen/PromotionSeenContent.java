package com.fps.opendagen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PromotionSeenContent {

    /**
     * A PromotionSeen item representing a piece of content.
     */
    public static class PromotionSeenItem {
        public final String name;
        public final String title;
        public final String message;
        public final String uri;

        public PromotionSeenItem(String name, String title, String message, String uri) {
            this.name = name;
            this.title = title;
            this.message = message;
            this.uri = uri;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
