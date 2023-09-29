package com.ma.rovers.repositories

import com.ma.rovers.domain.IField

class InMemoryFieldsRepository: IFieldsRepository {
    companion object {
        var storedField: IField? = null
    }
    override fun save(field: IField) {
        storedField = field
    }

    override fun restore(): IField {
        if (storedField == null) throw NoSuchElementException("There's no any field stored in database")
        return storedField as IField
    }

    override fun delete() {
        storedField = null
    }
}