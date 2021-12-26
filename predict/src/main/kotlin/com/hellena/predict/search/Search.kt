package com.hellena.predict.search

interface Search {
    fun getPage(): Page;
}

interface Page {
    fun getIndex(): Integer;

    fun getSize(): Integer;

    fun getSort(): List<Sort>;
}

interface Sort {
    fun getName(): String;

    fun getDir(): SortDirection;
}

enum class SortDirection {
    ASC, DESC;
}
