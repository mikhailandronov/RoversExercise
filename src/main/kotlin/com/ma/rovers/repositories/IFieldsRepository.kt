package com.ma.rovers.repositories

import com.ma.rovers.domain.IField

interface IFieldsRepository {
    fun save (field: IField)
    fun restore (): IField
    fun delete()
}