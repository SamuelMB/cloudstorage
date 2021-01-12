package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.BusinessException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserService userService, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    public int createCredential(Credential credential, String username) throws BusinessException {
        User user = userService.getUser(username);
        if(user == null) {
            throw new BusinessException("User not exist!");
        }
        Credential credentialReturned = credentialMapper.getCredentialByUsernameAndUserId(credential.getUsername(), user.getUserId());
        if(credentialReturned != null) {
            throw new BusinessException("Credential already exist!");
        }
        String key = generateKey();
        String passwordEncrypted = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setUserId(user.getUserId());
        credential.setKey(key);
        credential.setPassword(passwordEncrypted);
        return credentialMapper.insert(credential);
    }

    public int updateCredential(Credential credential, String username) throws BusinessException {
        User user = userService.getUser(username);
        if(user == null) {
            throw new BusinessException("User not found!");
        }
        Credential credentialReturned = credentialMapper.getByCredentialId(credential.getCredentialId());
        if(credentialReturned != null) {
            if(!credential.getPassword().equals(credentialReturned.getPassword())) {
                String key = generateKey();
                String passwordEncrypted = encryptionService.encryptValue(credential.getPassword(), key);
                credential.setKey(key);
                credential.setPassword(passwordEncrypted);
            }
            return credentialMapper.update(credential);
        }
        throw new BusinessException("Credential not found!");
    }

    public int deleteByCredentialId(Long credentialId, String username) throws BusinessException {
        User user = userService.getUser(username);
        if(user != null) {
            return credentialMapper.deleteByCredentialId(credentialId);
        } else {
            throw new BusinessException("User not found!");
        }
    }

    public List<Credential> getAllByUsername(String username) {
        User user = userService.getUser(username);
        if(user != null) {
            return credentialMapper.getAllByUserId(user.getUserId());
        }
        return new ArrayList<>();
    }

    public String generateKey() {
        byte[] nonce = new byte[16];
        new SecureRandom().nextBytes(nonce);
        StringBuilder result = new StringBuilder();
        for (byte temp : nonce) {
            result.append(String.format("%02x", temp));
        }
        return result.toString();
    }
}
