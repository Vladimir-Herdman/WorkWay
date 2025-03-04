package com.zybooks.workway.database


import android.content.Context
import android.util.Log
import com.zybooks.workway.model.Company
import com.zybooks.workway.model.User
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class CompanyDB(context: Context) {

    private val path = "/data/data/com.zybooks.workway/db/company_db"
    private val url = "jdbc:sqlite:$path"
//    private val user = "root"
//    private val password = ""
    private val dbHelper = DBHelper(context)
    private var connection: Connection? = null

    init {
        connect()
        createTable()
    }

    private fun connect() {
        try {
//            connection = DriverManager.getConnection(url, user, password)
            connection = DriverManager.getConnection(url)
            println("Connected to Company Database!")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to connect to Company Database. ${e.message}")
        }
    }

    private fun createTable() {
        val sql = """
            CREATE TABLE IF NOT EXISTS companies (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                logo TEXT,
                description TEXT,
                business_code TEXT UNIQUE NOT NULL,
                domain TEXT UNIQUE NOT NULL
            );
        """
        connection?.createStatement()?.execute(sql)
    }

    fun getAllCompanies(){
        val companyList = mutableListOf<Company>()

        val sql = "SELECT * FROM companies"
        val statement: PreparedStatement? = connection?.prepareStatement(sql)
        val resultSet: ResultSet? = statement?.executeQuery()

        Log.d("CompanyDBTest", "Fetching all companies... $resultSet")

    }

    fun insertCompany(title: String, logo: String?, description: String, businessCode: String, domain: String): Boolean {
        val sql = "INSERT INTO companies (title, logo, description, business_code, domain) VALUES (?, ?, ?, ?, ?)"
        val statement: PreparedStatement? = connection?.prepareStatement(sql)
        statement?.apply {
            setString(1, title)
            setString(2, logo)
            setString(3, description)
            setString(4, businessCode)
            setString(5, domain)
        }
        return (statement?.executeUpdate() ?: 0) > 0
    }

    fun authenticateUser(email: String, password: String, businessCode: String): String {

        val domain = email.substringAfter("@") // Extract domain from email

//        Check if compnay exist
        val companyQuery = "SELECT business_code FROM company WHERE domain = ?"
        val companyStatement: PreparedStatement? = connection?.prepareStatement(companyQuery)
        companyStatement?.setString(1, domain)
        val companyResultSet: ResultSet? = companyStatement?.executeQuery()

        if (companyResultSet?.next() != true) {
            return "Your company is not on our platform."
        }

        val storedBusinessCode = companyResultSet.getString("business_code")

//        Verify business code
        if (storedBusinessCode != businessCode) {
            return "Wrong business code."
        }

//        Authenticate User
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


    fun insertDummyCompanies() {
        val sql = "INSERT INTO company (title, description, business_code, domain, logo) VALUES (?, ?, ?, ?, ?, ?)"
        val statement: PreparedStatement? = connection?.prepareStatement(sql)

        val companies = listOf(
            Triple("onu.edu", "Ohio Northern University", "A private university in Ohio."),
            Triple("osu.edu", "Ohio State University", "A public research university in Ohio."),
            Triple("ibm.com", "IBM", "A multinational technology company.")
        )

        try {
            for ((index, company) in companies.withIndex()) {
                statement?.apply {
                    setString(2, company.second) // Title
                    setString(3, company.third) // Description
                    setString(4, "BC${1000 + index}") // Dummy business code
                    setString(5, company.first) // Domain
                    setString(6, "logo${index + 1}.png") // Dummy logo filename
                    executeUpdate()
                }
            }
            println("Dummy companies inserted successfully!")
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            statement?.close()
        }
    }

}
