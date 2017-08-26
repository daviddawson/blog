package mytown.basket

import mytown.product.Product
import mytown.product.ProductQueryService
import spock.lang.Specification

class BasketSpec extends Specification {

    def "add products, calculates correctly" () {

        def productService = Mock(ProductQueryService) {
            getProduct("1") >> new Product(id: "1", price: 12.3)
            getProduct("2") >> new Product(id: "2", price: 1.5)
            getProduct("3") >> new Product(id: "3", price: 9.4)
        }

        def basket = new Basket()

        when:
        basket.addProduct(productService.getProduct("1"))
        basket.addProduct(productService.getProduct("2"))
        basket.addProduct(productService.getProduct("3"), 5)

        and:
        def calc = basket.getCalc(productService)

        then:
        //lines
        calc.items.size() == 3
        calc.tax == 12.16
        calc.subtotal == 60.8
        calc.total == 72.96
    }

}
