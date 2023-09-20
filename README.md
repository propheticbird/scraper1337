## What

Basic scraper for https://books.toscrape.com/ webpage written in Java.

## How to run

Prerequisites: Docker Desktop

Build a Docker image from the local Dockerfile (jar is under version control):

`docker build -t books_to_scrape:latest .`

Run Docker container:

`docker run -it --rm --init -p 3000:3000 books_to_scrape`

When execution is finished, a catalog named `books_to_scrape` shall appear in the current directory,
navigate to it and open `index.html` file to view the website.

## Thoughts

Scraper parses first index page and creates an object with page resources and links to other html pages. 
When scraper visits a page it is added to the set of visited urls to avoid serializing same page twice. After processing
the first page, spawn a thread pool to process links to other pages.

## What went wrong

* No commit history. I haven't used the `Jsoup` library before, 
and it was a while that I used `java.io` and `java.nio` packages, so it was a lot of trial and error which ended up in
multiple versions of the BooksScraper class. Can demonstrate older versions during the call if needed. Usually, I am more
consistent with committing changes when achieving some logical milestone.

* Exit from the recursion and large memory footprint.

* No tests due to time constraints.