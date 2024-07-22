package com.product.crud_project;

import com.product.crud_project.entity.Product;
import com.product.crud_project.repository.ProductRepository;
import com.product.crud_project.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CrudProjectApplicationTests {
	@Autowired
	private ProductService productservice;

	@MockBean
	private ProductRepository productRepository;


	@Test
	public void getProductTest() {
		when(productRepository.findAll()).thenReturn((List<Product>) of(new Product(555,"sweet",10,100)).collect(Collectors.toList()));
		assertEquals(1, productservice.getProduct().size());
	}

	@Test
	public void getProductByIdTest() {
		int id = 55; // Corrected to integer value
		Product product = new Product(55, "sweeet", 100, 1000);
		when(productRepository.findById(id)).thenReturn(Optional.of(product));
		assertEquals(product, productservice.getProductById(id));
	}

	@Test
	public void getProductByNameTest() {
		String name = "sweeet";
		Product product = new Product(55, name, 100, 1000);
		when(productRepository.findByName(name)).thenReturn(product);

		Product result = productservice.getProductByName(name);
		assertEquals(product, result);
	}

	@Test
	public void saveProductTest() {
		Product product = new Product(55, "sweeet", 100, 1000);
		when(productRepository.save(product)).thenReturn(product);

		Product result = productservice.saveProduct(product);
		assertEquals(product, result);
	}

	@Test
	public void saveProductsTest() {
		List<Product> products = Arrays.asList(
				new Product(1, "product1", 100, 1000),
				new Product(2, "product2", 200, 2000)
		);

		when(productRepository.saveAll(products)).thenReturn(products);

		List<Product> result = productservice.saveProducts(products);
		assertEquals(products, result);
	}

	@Test
	public void deleteProductTest() {
		int id = 55;
		doNothing().when(productRepository).deleteById(id);

		String result = productservice.deleteProduct(id);

		verify(productRepository).deleteById(id);
		assertEquals("Product removed !! " + id, result);
	}

	@Test
	public void updateProductTest() {
		// Create a sample product
		Product updatedProduct = new Product(1, "Updated Product", 200, 2000);

		// Mock behavior for findById
		Product existingProduct = new Product(1, "Existing Product", 100, 1000);
		when(productRepository.findById(updatedProduct.getId())).thenReturn(java.util.Optional.of(existingProduct));

		// Mock behavior for save
		when(productRepository.save(existingProduct)).thenReturn(updatedProduct);

		// Call the updateProduct method
		Product result = productservice.updateProduct(updatedProduct);

		// Verify the save method was called with the correct product
		verify(productRepository).save(existingProduct);

		assertEquals(updatedProduct, result);
		assertEquals("Updated Product", result.getName());
		assertEquals(200, result.getQuantity());
		assertEquals(2000, result.getPrice());
	}

}
