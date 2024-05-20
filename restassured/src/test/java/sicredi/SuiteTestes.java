package sicredi;

import org.junit.BeforeClass;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.json.simple.JSONObject;

public class SuiteTestes {

	@BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://dummyjson.com";
    }
	
	@Test
	public void buscarStatusAplicacao() {
		 given()
         .when()
           .get("/test")
         .then()
           .statusCode(200) 
           .body("status", equalTo("ok")) 
           .body("method", equalTo("GET"));
	}
	
	@Test
	public void buscarUsuarios() {
		 given()
         .when()
           .get("/users")
         .then()
           .statusCode(200)
           .contentType(ContentType.JSON)
           .body("total", greaterThan(0)); 
 }
	
	@SuppressWarnings("unchecked")
	@Test
	public void criacaoTokenAutenticacao() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("username", "kminchelle");
		requestParams.put("password", "0lelplR");
		given()
        .header("Content-Type", "application/json")
          .body(requestParams.toJSONString())
        .when()
          .post("/auth/login")
        .then()
          .statusCode(200) 
          .body("username", equalTo("kminchelle")) 
          .body("email", equalTo("kminchelle@qq.com"))
          .body("firstName", equalTo("Jeanne"))
          .body("lastName", equalTo("Halvorson"))
          .body("gender", equalTo("female"))
          .body("image", equalTo("https://robohash.org/Jeanne.png?set=set4"))
          .body("token", not("")); 
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void criacaoTokenAutenticacaoSenhaInvalida() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("username", "kminchelle");
		requestParams.put("password", "senha invalida");
		given()
          .header("Content-Type", "application/json")
          .body(requestParams.toJSONString())
        .when()
        .  post("/auth/login")
        .then()
          .statusCode(400) 
          .body("message", equalTo("Invalid credentials"));
	}
	
	@Test
	public void buscarProdutosComAutenticacao() {
		String token = getToken();
		given()
          .header("Authorization", "Bearer "+token)
          .header("Content-Type","application/json")
        .when()
          .get("/auth/products")
        .then()
          .statusCode(200) 
          .contentType(ContentType.JSON)
          .body("total", greaterThan(0)); 
		
	}
	
	@Test
	public void buscarProdutosComAutenticacaoTokenInvalido() {
		String token = "iyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTUsInVzZXJuYW1lIjoia21pbmNoZWxsZSIsImVtYWlsIjoia21pbmNoZWxsZUBxcS5jb20iLCJmaXJzdE5hbWUiOiJKZWFubmUiLCJsYXN0TmFtZSI6IkhhbHZvcnNvbiIsImdlbmRlciI6ImZlbWFsZSIsImltYWdlIjoiaHR0cHM6Ly9yb2JvaGFzaC5vcmcvSmVhbm5lLnBuZz9zZXQ9c2V0NCIsImlhdCI6MTcxMjQxMjgzNiwiZXhwIjoxNzEyNDE2NDM2fQ.0sCqd6r2gKDfW-G6O26m821CGh47pDth_duYRe4WNRM";
			
		given()
          .header("Authorization", "Bearer "+token)
          .header("Content-Type","application/json")
        .when()
          .get("/auth/products")
        .then()
          .statusCode(500) 
          .contentType(ContentType.JSON)
          .body("message",equalTo( "invalid token")); 
		
	}

	@Test
	@SuppressWarnings("unchecked")
	public void criacaoProduto() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("title", "Perfume Oil");
		requestParams.put("description", "Mega Discount, Impression of A...");
		requestParams.put("price", "13.0");
		requestParams.put("discountPercentage", "8.4");
		requestParams.put("rating", "4.26");
		requestParams.put("stock", "65");
		requestParams.put("brand", "Impression of Acqua Di Gio");
		requestParams.put("category", "fragrances");
		requestParams.put("thumbnail", "https://i.dummyjson.com/data/products/11/thumnail.jpg");
		 given()
           .header("Content-Type","application/json")
           .body(requestParams.toJSONString())
         .when()
           .post("/products/add")
         .then()
           .statusCode(200) ;
	}
	
	@Test
	public void buscarTodosProdutos() {
		given()
		  .header("Content-Type","application/json")
        .when()
          .get("/products") 
        .then()
          .statusCode(200) 
          .body("total", greaterThan(0)); 
	}
	
	@Test
	public void buscarApenasUmProdutoPorID() {
		given()
		  .header("Content-Type","application/json")
        .when()
          .get("/products/1") 
        .then()
          .statusCode(200) 
          .body("title", equalTo("iPhone 9")) 
          .body("description", containsString("An apple mobile")) 
          .body("price", equalTo(549)) 
          .body("discountPercentage", equalTo(12.96f)) 
          .body("rating", equalTo(4.69f)) 
          .body("stock", equalTo(94)) 
          .body("brand", equalTo("Apple")) 
          .body("category", equalTo("smartphones")) 
          .body("thumbnail", equalTo("https://cdn.dummyjson.com/product-images/1/thumbnail.jpg")) 
          .body("images", hasItems(
            "https://cdn.dummyjson.com/product-images/1/1.jpg",
            "https://cdn.dummyjson.com/product-images/1/2.jpg",
            "https://cdn.dummyjson.com/product-images/1/3.jpg",
            "https://cdn.dummyjson.com/product-images/1/4.jpg",
            "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        )); 

	}
	
	@Test
	public void buscarApenasUmProdutoPorIdInvalido() {
		given()
		  .header("Content-Type","application/json")
        .when()
          .get("/products/0") 
        .then()
          .statusCode(404) 
          .body("message", equalTo("Product with id '0' not found")); 
       

	}

	
	@SuppressWarnings("unchecked")
	public String getToken() {
		RestAssured.baseURI = "https://dummyjson.com";
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		JSONObject requestParams = new JSONObject();
		requestParams.put("username", "kminchelle");
		requestParams.put("password", "0lelplR");
		request.body(requestParams.toJSONString());
		Response response = request.post("/auth/login");
		String token = response.jsonPath().getString("token");
		return token;

	}

////teste jenkins 1
	
	//teste webhook

  ///gdfgfgfg
  //gfddfg

  teste
	}


