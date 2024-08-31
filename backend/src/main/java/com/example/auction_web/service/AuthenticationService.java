package com.example.auction_web.service;

import com.example.auction_web.dto.request.AuthenticationRequest;
import com.example.auction_web.dto.request.IntrospectRequest;
import com.example.auction_web.dto.response.AuthenticationResponse;
import com.example.auction_web.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
