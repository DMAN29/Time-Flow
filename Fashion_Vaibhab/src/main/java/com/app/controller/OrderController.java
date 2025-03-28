package com.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.exception.OrderException;
import com.app.model.Order;
import com.app.request.AllowanceRequest;
import com.app.request.LaneRequest;
import com.app.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping("")
	public ResponseEntity<Order> createOrder(@RequestBody Order order) throws OrderException{
		return new ResponseEntity<Order>(orderService.createOrder(order),HttpStatus.CREATED);
	}
	
	@GetMapping("")
	public ResponseEntity<List<Order>> getAllOrder(){
		return new ResponseEntity<>(orderService.getAllorder(),HttpStatus.OK);
	}
	
	@GetMapping("/style/{styleNo}")
	public ResponseEntity<Order> getOrderByStyleNo(@PathVariable String styleNo) throws OrderException{
		return new ResponseEntity<Order>(orderService.getOrderByStyleNo(styleNo),HttpStatus.OK);
	}
	
	@GetMapping("/item/{itemNo}")
	public ResponseEntity<Order> getOrderByItemNo(@PathVariable String itemNo) throws OrderException{
		return new ResponseEntity<Order>(orderService.getOrderByItemNo(itemNo),HttpStatus.OK);
	}
	
	@PutMapping("/upload/{styleNo}")
	public ResponseEntity<Order> updateOperations(@RequestParam("file") MultipartFile file,@PathVariable String styleNo) throws OrderException, IOException{
        return new ResponseEntity<Order>(orderService.updateOperations(file,styleNo),HttpStatus.ACCEPTED);
    }
	
	@PutMapping("/allowance")
	public ResponseEntity<String> updateAllowance(@RequestBody AllowanceRequest allowance) throws OrderException{
		return new ResponseEntity<String>(orderService.updateAllowance(allowance),HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/lane")
	public ResponseEntity<String> updateLane(@RequestBody LaneRequest lane) throws OrderException{
		return new ResponseEntity<String>(orderService.updateLane(lane),HttpStatus.ACCEPTED);
	}
}
