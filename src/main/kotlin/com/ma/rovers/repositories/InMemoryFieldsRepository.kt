package com.ma.rovers.repositories

import com.ma.rovers.domain.Field
import com.ma.rovers.domain.IField
import com.ma.rovers.domain.IntSize

class InMemoryFieldsRepository: IFieldsRepository {
    companion object {
        var storedField: IField? = null
    }
    override fun save(field: IField) {
        storedField = field
    }

    override fun restore(): IField {
        if (storedField == null) throw NoSuchElementException("There's no any field stored in database")
        //return storedField as IField
        return Field(IntSize(storedField!!.length.size), IntSize(storedField!!.width.size))
    }

    override fun delete() {
        storedField = null
    }
}