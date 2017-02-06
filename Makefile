
run:
	jekyll clean
	jekyll serve --incremental

clean:
	jekyll clean

render:
	jekyll clean
	jekyll build

setup:
	sudo gem install jekyll jekyll-redirect-from jekyll-paginate jekyll-sitemap jekyll-asciidoc asciidoctor-diagram pygments.rb

prepare:
	rm -rf /tmp/blog-gen
	mkdir /tmp/blog-gen
	git clone -b gh-pages git@github.com:daviddawson/blog.git /tmp/blog-gen
	$(cd /tmp/blog-gen; git checkout gh-pages)

install: prepare render
	./buildsite.sh
