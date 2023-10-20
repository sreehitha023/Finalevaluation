package com.msil.evaluation.constants;

public class ErrorConstants {
    public static final String Invalid_Email = "Invalid email format.Enter an email address in the proper format.";
    public static final String Quantity_Constraint = "Quantity must be an integer";
    public static final String Order_Operation = "Please add correct operation";
    public static final String Password_Pattern = "^(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String Username_Blank = "The UserName field should not be black";
    public static final String Password_Constraint = "The password does not match the expectations.";
    public static final String Password_Blank = "The password field cannot be left empty.";
    public static final String User_NotFound = "User not found";
    public static final String User_Not_Logged_In = "User has not logged in";
    public static final String Stock_Symbol_Not_Blank = "Stock symbol should not be blank.";
    public static final String Stock_Symbol_Length = "Stock symbol length should be between 10 and 25 characters.";
    public static final String Order_Type = "Invalid order type. It should be 'buy' or 'sell'.";
    public static final String Price_Positive = "Price must be a positive value.";
    public static final String Price_Constraint = "Price should be greater than or equal to 0.0.";
    public static final String Failure = "FAILED";
    public static final String Symbols_Import_Error = "Error importing symbols: ";
    public static final String Group_Id_Not_Found = "The specified groupId is not added by :";
    public static final String Symbol_Not_Found = "The mentioned symbol does not exist add correct symbol";
    public static final String Symbol_Already_Exists = "The mentioned symbol already exists in watchList .";
    public static final String Group_Id_Null = "GroupId cannot be null, provide valid groupId";
    public static final String Order_NotFound = "Order not found";
    public static final String Order_Integer = "Enter integer value for orderId";
    public static final String User_Already_Logged_In = "User already logged in";
    public static final String Email_Already_Exists = "Email Id Already exists";
    public static final String Username_Already_Exists = "Username Already exists";
}
