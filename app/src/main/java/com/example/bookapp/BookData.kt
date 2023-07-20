package com.example.bookapp

class BookData {
    companion object {
        fun getBooks(): ArrayList<BookModel> = arrayListOf(
            BookModel(1, "https://m.media-amazon.com/images/I/719njS5bQhL._SY522_.jpg","The Little Prince", "https://upload.wikimedia.org/wikipedia/commons/7/7f/11exupery-inline1-500.jpg", "Antoine De Saint-Exupery", "9780060555665",
                BookType.KIDS, null
            ),
            BookModel(2, "https://libris.to/media/jacket/04008460_intelligent-investor.jpg","The Intelligent Investor", "https://upload.wikimedia.org/wikipedia/commons/2/2a/Benjamin_Graham_%281894-1976%29_portrait_on_23_March_1950.jpg", "Benjamin Grahm", "9780060555665",
                BookType.FINANCIAL_EDUCATION, null
            ),
            BookModel(3, "https://libris.to/media/jacket/21063216_happiness-project-the-10th-anniversary-edition.jpg","The Happiness Project", "https://gretchenrubin.com/wp-content/uploads/elementor/thumbs/gretche-rubin_hero_homepage-q2953oqvxvbipoj8k2ayi78po84bdyu301or10q67c.jpg", "Gretchen Rubin", "9780061583254",
                BookType.SELF_HELP_BOOK, null
            ),
            BookModel(4, "https://cdn4.libris.ro/img/pozeprod/1207/1206116-1.jpg","The Little Book That Still beats The Market", "https://www.worldtopinvestors.com/wp-content/uploads/2018/01/Joel-Greenblatt-world-top-investors.jpg", "Joel Greenblatt", "9780060555665",
                BookType.FINANCIAL_EDUCATION, null
            ),
            BookModel(5, "https://cdn4.libris.ro/img/pozeprod/10860/10859251-1.jpg","The 5 Love Languages", "https://upload.wikimedia.org/wikipedia/commons/5/51/Gary_D._Chapman.jpg", "Gary Chapman", "9780789919779",
                BookType.SELF_HELP_BOOK, null
            ),
            BookModel(6, "https://cdn4.libris.ro/img/pozeprod/10845/10844464-1.jpg","The Little Book of Common Sense Investing", "https://ei.marketwatch.com/Multimedia/2018/11/29/Photos/ZG/MW-GZ333_bogle__20181129124951_ZG.jpg?uuid=2bfdb2e6-f3ff-11e8-a4e5-ac162d7bc1f7", "John C. Bogle", "9780060555665",
                BookType.FINANCIAL_EDUCATION, null
            )
        )
    }
}