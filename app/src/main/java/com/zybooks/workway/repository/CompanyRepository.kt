package com.zybooks.workway.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.zybooks.workway.database.DBHelper
import com.zybooks.workway.model.Company

class CompanyRepository(context: Context) {
    private val dbHelper = DBHelper(context)

    fun insertCompany(company: Company): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_COMPANY_TITLE, company.title)
            put(DBHelper.COLUMN_COMPANY_LOGO, company.logo)
            put(DBHelper.COLUMN_COMPANY_DESCRIPTION, company.description)
            put(DBHelper.COLUMN_COMPANY_BUSINESS_CODE, company.businessCode)
            put(DBHelper.COLUMN_COMPANY_DOMAIN, company.domain)
        }
        return db.insert(DBHelper.TABLE_COMPANY, null, values)
    }

    fun getCompanyByDomain(userDomain: String): Company? {
        println("Getting company by domain: $userDomain")
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DBHelper.TABLE_COMPANY,
            arrayOf(
                DBHelper.COLUMN_COMPANY_ID,
                DBHelper.COLUMN_COMPANY_TITLE,
                DBHelper.COLUMN_COMPANY_LOGO,
                DBHelper.COLUMN_COMPANY_DESCRIPTION,
                DBHelper.COLUMN_COMPANY_BUSINESS_CODE,
                DBHelper.COLUMN_COMPANY_DOMAIN
            ),
            "${DBHelper.COLUMN_COMPANY_DOMAIN} = ?",
            arrayOf(userDomain),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val company = Company().apply {
                id = cursor.getInt(0)
                title = cursor.getString(1)
                logo = cursor.getString(2)
                description = cursor.getString(3)
                businessCode = cursor.getString(4)
                domain = cursor.getString(5)
            }
            cursor.close()
            company
        } else {
            cursor.close()
            null
        }
    }

    fun authenticateUser(email: String, password: String, businessCode: String): String {
        val domain = email.substringAfter("@") // Extract domain from email

        // Check if company exists
        val company = getCompanyByDomain(domain)
        if (company == null) {
            return "Company not found."
        }

        // Verify business code
        if (company.businessCode != businessCode) {
            return "Wrong business code."
        }

        // Authenticate User
        val userCursor = dbHelper.getData(
            "users",
            arrayOf("email"),
            "email = ? AND password = ? AND business_code = ?",
            arrayOf(email, password, businessCode)
        )

        val isAuthenticated = userCursor.moveToFirst() // Moves cursor to first row if data exists
        userCursor.close()

        return if (isAuthenticated) {
            "Authentication successful."
        } else {
            "Invalid email or password."
        }
    }
}
