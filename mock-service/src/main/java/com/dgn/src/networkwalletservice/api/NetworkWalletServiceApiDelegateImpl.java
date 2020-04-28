package com.dgn.src.networkwalletservice.api;

import com.dgn.src.networkwalletservice.model.*;
import com.dgn.src.networkwalletservice.model.Error;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class NetworkWalletServiceApiDelegateImpl implements NetworkWalletServiceApiDelegate {

    @Override
    public ResponseEntity<SRCAccountProvisionResponse> provisionAccount(SRCAccountProvisionRequest request, String accept, String contentType, String cacheControl, String authorization, String X_DFS_C_APP_CERT, String X_DFS_API_PLAN) {
        SecureContext requestSecureContext = null;
        String cardPanNumber = null;
        try {
            AccountProvisionRequest provisionRequest = request.getAccountProvisionRequest();
            requestSecureContext = provisionRequest.getSecureContext();
            AccountContext accountContext = requestSecureContext.getEncryptedContent();
            cardPanNumber = accountContext.getPan();
        } catch (Exception e) {
            SRCAccountProvisionResponse response = initializeProvisionErrResponse(request);
            Error responseErr = new Error();
            responseErr.setErrorCode("10001");
            responseErr.setErrorMessage("Something is missing in the request.");
            response.getErrors().add(responseErr);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isNoneEmpty(cardPanNumber)) {
            cardPanNumber = cardPanNumber.trim();
        } else {
            SRCAccountProvisionResponse response = initializeProvisionErrResponse(request);
            Error responseErr = new Error();
            responseErr.setErrorCode("10001");
            responseErr.setErrorMessage("accountProvisionRequest.secureContext.encryptedContent.pan is empty");
            response.getErrors().add(responseErr);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        String flowCardNumberPrefix = cardPanNumber.substring(0,5);
        if ("60111".equals(flowCardNumberPrefix) ) {
            SRCAccountProvisionResponse response = initializeProvisionResponse(request);
            AccountProvisionResponse responseBody = new AccountProvisionResponse();
            response.setAccountProvisionResponse(responseBody);
            responseBody.setProvisioningDecision("APPROVED");
            setProvisioningMetadataContext(responseBody);
            setAccountMetaDataContext(responseBody);
            setIssuerContext(responseBody);
            responseBody.setSecureContext(requestSecureContext);
            return new ResponseEntity(response, HttpStatus.OK);
        } else if ("60112".equals(flowCardNumberPrefix) ) {
            SRCAccountProvisionResponse response = initializeProvisionResponse(request);
            AccountProvisionResponse responseBody = new AccountProvisionResponse();
            response.setAccountProvisionResponse(responseBody);
            responseBody.setProvisioningDecision("OOB");
            setProvisioningMetadataContext(responseBody);
            setAccountMetaDataContext(responseBody);
            setIssuerContext(responseBody);
            responseBody.setSecureContext(requestSecureContext);
            return new ResponseEntity(response, HttpStatus.OK);
        } else if ("60113".equals(flowCardNumberPrefix) ) {
            SRCAccountProvisionResponse response = initializeProvisionResponse(request);
            AccountProvisionResponse responseBody = new AccountProvisionResponse();
            response.setAccountProvisionResponse(responseBody);
            responseBody.setProvisioningDecision("DECLINED");
            return new ResponseEntity(response, HttpStatus.OK);
        } else {
            SRCAccountProvisionResponse response = initializeProvisionErrResponse(request);
            Error responseErr = new Error();
            responseErr.setErrorCode("10001");
            responseErr.setErrorMessage("accountProvisionRequest.secureContext.encryptedContent.pan is not green, nor yellow, nor red");
            response.getErrors().add(responseErr);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    private void setIssuerContext(AccountProvisionResponse responseBody) {
        IssuerContext issuer = new IssuerContext();
        issuer.setContactNumber("800-867-5309");
        issuer.setEmail("jondaun@discover.com");
        issuer.setIssuerName("Discover Network");
        issuer.setPrivacyPolicyURL("https://www.discover.com/privacypolicy.html");
        issuer.setTermsConditionsURL("https://www.discover.com/privacypolicy.html");
        issuer.setWebsite("https://www.discover.com");
        responseBody.setIssuerContext(issuer);
    }

    private void setAccountMetaDataContext(AccountProvisionResponse responseBody) {
        AccountMetadataContext accountMetaData = new AccountMetadataContext();
        accountMetaData.setCardType("Credit");
        Color backgroundColor = new Color();
        backgroundColor.setBlue("789");
        backgroundColor.setRed("123");
        backgroundColor.setGreen("456");
        accountMetaData.setBackgroundColorRGB(backgroundColor);
        Color foregroundColor = new Color();
        foregroundColor.setBlue("789");
        foregroundColor.setRed("123");
        foregroundColor.setGreen("456");
        accountMetaData.setForegroundColorRGB(foregroundColor);
        Color labelColor = new Color();
        labelColor.setBlue("789");
        labelColor.setRed("123");
        labelColor.setGreen("456");
        accountMetaData.setLabelColorRGB(labelColor);
        accountMetaData.setProductDescription("Discover It");
        accountMetaData.setCardImageId("df106a670805440d8cdf1f8647c24060");
        accountMetaData.setNewPanExpiry("1023");
        accountMetaData.setPanSuffix("0289");
        accountMetaData.setTokenSuffix("0004");
        responseBody.setAccountMetadataContext(accountMetaData);
    }

    private void setProvisioningMetadataContext(AccountProvisionResponse responseBody) {
        ProvisioningMetadataContext provisionMetaData = new ProvisioningMetadataContext();
        provisionMetaData.setPanId("df106a60805440dcdf1f864c123ads");
        provisionMetaData.setTokenId("df106a60805440dcdf1f864c24060");
        provisionMetaData.setTermsAndConditionsId("e00fee88-67c6-42a6-bfc2-00a078a7c2ff");
        responseBody.setProvisioningMetadataContext(provisionMetaData);
    }

    @NotNull
    private SRCAccountProvisionResponse initializeProvisionResponse(SRCAccountProvisionRequest request) {
        SRCAccountProvisionResponse response = new SRCAccountProvisionResponse();
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setResponseId(UUID.randomUUID().toString());
        RequestHeader requestHeader = request.getRequestHeader();
        responseHeader.setProgramId(requestHeader.getProgramId());
        responseHeader.setSessionId(requestHeader.getSessionId());
        responseHeader.setUserContext(requestHeader.getUserContext());
        response.setResponseHeader(responseHeader);
        return response;
    }

    @NotNull
    private SRCAccountProvisionResponse initializeProvisionErrResponse(SRCAccountProvisionRequest request) {
        SRCAccountProvisionResponse response = new SRCAccountProvisionResponse();
        response.setErrors(new ArrayList());
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setResponseId(UUID.randomUUID().toString());
        RequestHeader requestHeader = request.getRequestHeader();
        responseHeader.setProgramId(requestHeader.getProgramId());
        responseHeader.setSessionId(requestHeader.getSessionId());
        responseHeader.setUserContext(requestHeader.getUserContext());
        response.setResponseHeader(responseHeader);
        return response;
    }
}