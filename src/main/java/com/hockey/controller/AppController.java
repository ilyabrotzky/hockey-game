package com.hockey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hockey.model.dto.AttentionNewDTO;
import com.hockey.model.entity.Attention;
import com.hockey.model.vo.AttentionVO;
import com.hockey.service.AttentionService;
import com.hockey.service.SeatService;
import com.hockey.utils.UserUtils;

@Controller
@RequestMapping("/app")
public class AppController {

	@Autowired
	private SeatService seatService;
	@Autowired
	private AttentionService attentionService;
	@Autowired
	private SimpMessagingTemplate template;

	@GetMapping("/seat-numbers")
	public ResponseEntity<?> seatNumber() {
		return new ResponseEntity<>(seatService.listSeatNumberVO(), HttpStatus.OK);
	}

	@GetMapping("/seat-ranking")
	public ResponseEntity<?> seatRanking() {
		return new ResponseEntity<>(seatService.listSeatRankingVO(), HttpStatus.OK);
	}

	@PostMapping("/invert-notification-status")
	public ResponseEntity<?> invertNotificationStatus() {
		return new ResponseEntity<>(seatService.invertNotificationStatusFrom(seatService.findById(UserUtils.getUser().getUsername())), HttpStatus.OK);
	}

	@PostMapping("/new-attention")
	public ResponseEntity<?> newAttention(@RequestBody AttentionNewDTO attention) {
		if (attention != null) {
			Attention att = attentionService.attentionNew(attention);

			if (att != null) {
				template.convertAndSend("/attention-client", new AttentionVO(att));
				return ResponseEntity.status(HttpStatus.CREATED).body(att);
			}
		}

		return ResponseEntity.notFound().build();

	}

	@GetMapping("/seat-points-balance")
	public ResponseEntity<?> findSeatBalance() {
		return new ResponseEntity<>(seatService.findSeatBalance(UserUtils.getUser().getUsername()), HttpStatus.OK);
	}

}