
.PHONY: images

run: images
	jekyll clean
	jekyll serve --drafts --incremental

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

images:
	$(MAKE) -C $@
