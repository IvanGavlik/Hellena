package com.hellena.predict.fetch;

import com.hellena.predict.item.Item

/**
 * Fetch items from store
 */
interface Fetch {
    fun fetch(): List<Item>;
}
