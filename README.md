# ISC4U-Summative June 13 2023 patch


* Books.java was edited
    * Currently in the viewBooks table there were button(s) that didn't do anything. But now with the 2 additional methods of LoanBook() and BorrowBook() in the Books class, the code can properly combine the interaction of the 2 text files as content moves from 1 file to another
 
* Users.java was edited

  * With the new Books text file/class the table in Users.java now can display users/password + any books they own. Speaking of, the accounts.txt file now accommodates books owned by users.
 

* The library GUI class had multiple action listeners attached to its buttons with the new methods in Books.java
