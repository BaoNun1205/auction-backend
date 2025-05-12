package com.example.auction_web.personalization.dto;

import com.example.auction_web.entity.AuctionSession;
import java.util.List;

public record AuctionSessionVector(AuctionSession session, List<Float> vector) {}

