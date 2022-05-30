package com.example

class Database(private val data: Data) {
    fun getAccount(): Account {
        return Account(
            books = data.account.ownedBooks.map { data.books[it.key] to it.value  }.map { (book, amount) ->
                Account.BookItem(
                    book = Account.BookItem.Book(
                        name = book.name,
                        author = book.author
                    ),
                    amount = amount
                )
            },
            balance = data.account.money
        )
    }
    data class Account(
        val books: List<BookItem>,
        val balance: Int
    ) {
        data class BookItem(
            val book: Book,
            val amount: Int,
        ) {
            data class Book(
                val name: String,
                val author: String,
            )
        }
    }
    fun getMarket(): Market {
        return Market(data.books.mapIndexed { id, book ->
            Market.Product(
                id = id,
                book = Market.Product.Book(
                    name = book.name,
                    author = book.author
                ),
                price = book.price,
                amount = book.amount
            )
        })
    }
    data class Market(
        val products: List<Product>
    ) {
        data class Product(
            val id: Int,
            val book: Book,
            val price: Int,
            val amount: Int,
        ) {
            data class Book(
                val name: String,
                val author: String,
            )
        }
    }
    data class DealParams(
        val id: Int,
        val amount: Int
    )
    fun makeDeal(deal: DealParams): Result<Unit> {
        val bookId = deal.id
        val book = data.books.getOrElse(bookId) {
            return Result.failure(DatabaseException("no book with id ${bookId}"))
        }
        if(book.amount < deal.amount) {
            return Result.failure(DatabaseException("not enough books. requested: ${deal.amount}, have ${book.amount}"))
        }
        val cost = deal.amount * book.price
        if(cost > data.account.money) {
            return Result.failure(DatabaseException("not enough money. required: ${cost}, have ${data.account.money}"))
        }
        data.account.money -= cost
        book.amount -= deal.amount
        val owned = data.account.ownedBooks.getOrPut(bookId) {
            0
        }
        data.account.ownedBooks[bookId] = owned + deal.amount
        return Result.success(Unit)
    }

    class DatabaseException(message: String): Exception(message)

}
//{
//    "products": [
//    {
//        "id": 0,
//        "book": {
//        "name": "Философия Java",
//        "author": "Брюс Эккель"
//    },
//        "price": 100,
//        "amount": 3
//    },
//    {
//        "id": 1,
//        "book": {
//        "name": "Думай медленно... Решай быстро",
//        "author": "Даниэль Канеман"
//    },
//        "price": 70,
//        "amount": 2
//    }
//    ]
//}

//{
//    "books": [
//    {
//        "book": {
//        "name": "Думай медленно... Решай быстро",
//        "author": "Даниэль Канеман"
//    },
//        "amount": 1
//    },
//    {
//        "book": {
//        "name": "Философия Java",
//        "author": "Брюс Эккель"
//    },
//        "amount": 2
//    }
//    ],
//    "balance": 30
//}