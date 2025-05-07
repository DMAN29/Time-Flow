package com.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.exception.OrderException;
import com.app.exception.UserException;
import com.app.model.Order;
import com.app.request.AllowanceRequest;
import com.app.request.EfficiencyRequest;
import com.app.request.LapsCountRequest;
import com.app.request.TargetRequest;
import com.app.response.ApiResponse;
import com.app.service.OrderService;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Order> createOrder(
	    @RequestHeader("Authorization") String jwt,
	    @RequestPart("order") Order order,
	    @RequestPart("file") MultipartFile file
	) throws OrderException, UserException, IOException {
	    return new ResponseEntity<>(orderService.createOrder(order, file, jwt), HttpStatus.CREATED);
	}


	
	@GetMapping("")
	public ResponseEntity<List<Order>> getAllOrder(@RequestHeader("Authorization")String token) throws UserException{
		return new ResponseEntity<>(orderService.getAllorder(token),HttpStatus.OK);
	}
	
	@GetMapping("/style/{styleNo}")
	public ResponseEntity<Order> getOrderByStyleNo(@PathVariable String styleNo) throws OrderException{
		return new ResponseEntity<Order>(orderService.getOrderByStyleNo(styleNo),HttpStatus.OK);
	}
	
	@GetMapping("/item/{itemNo}")
	public ResponseEntity<Order> getOrderByItemNo(@PathVariable String itemNo) throws OrderException{
		return new ResponseEntity<Order>(orderService.getOrderByItemNo(itemNo),HttpStatus.OK);
	}
	
//	@PutMapping("/upload/{styleNo}")
//	public ResponseEntity<Order> updateOperations(@RequestParam("file") MultipartFile file,@PathVariable String styleNo) throws OrderException, IOException{
//        return new ResponseEntity<Order>(orderService.updateOperations(file,styleNo),HttpStatus.ACCEPTED);
//    }
//	
	@PutMapping("/allowance")
	public ResponseEntity<ApiResponse> updateAllowance(@RequestBody AllowanceRequest allowance) throws OrderException{
		return new ResponseEntity<ApiResponse>(new ApiResponse(orderService.updateAllowance(allowance)),HttpStatus.ACCEPTED);
	}
	
//	@PutMapping("/lane")
//	public ResponseEntity<ApiResponse> updateLane(@RequestBody LaneRequest lane) throws OrderException{
//		return new ResponseEntity<ApiResponse>(new ApiResponse(orderService.updateLane(lane)),HttpStatus.ACCEPTED);
//	}
	
	@PutMapping("/laps")
	public ResponseEntity<ApiResponse> updateLapsCount(@RequestBody LapsCountRequest lap) throws OrderException{
		return new ResponseEntity<ApiResponse>(new ApiResponse(orderService.updateLapsCount(lap)),HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/delete/{styleNo}")
	public ResponseEntity<ApiResponse> deleteOrder(@PathVariable String styleNo,@RequestHeader("Authorization") String token) throws UserException, OrderException{
		orderService.deleteOrder(styleNo,token);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Order deleted With Style No: "+styleNo),HttpStatus.OK);
	}
	
	@PutMapping("/update/target")
	public ResponseEntity<ApiResponse> updateTarget(@RequestBody TargetRequest target,@RequestHeader("Authorization")String token) throws UserException, OrderException{
		orderService.updateTarget(target,token);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Order successfully updated to :"+target.getTarget()),HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/update/efficiency")
	public ResponseEntity<ApiResponse> updateEfficiency(@RequestBody EfficiencyRequest efficiency,@RequestHeader("Authorization")String token) throws UserException, OrderException{
		orderService.updateEfficiency(efficiency,token);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Order successfully updated to :"+efficiency.getEfficiency()),HttpStatus.ACCEPTED);
	}
}
