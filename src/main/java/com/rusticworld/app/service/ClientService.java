package com.rusticworld.app.service;

import com.rusticworld.app.dao.ClientDAO;
import com.rusticworld.app.dto.ClientDTO;
import com.rusticworld.app.model.ClientEntity;
import com.rusticworld.app.repository.ClientRepository;
import com.rusticworld.app.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientService {
    private ClientRepository clientRepository;

    public ResponseEntity<Object> save(ClientDTO clientDTO) {
        ClientEntity client = clientRepository.findByName(clientDTO.getName());
        if (client == null) {
            return ResponseEntity.ok().body(toDAO(clientRepository.save(fromDTO(clientDTO))));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(StringUtils.CLIENT_ALREADY_EXISTS + clientDTO.getName());
    }

    public ResponseEntity<Object> update(String name, ClientDTO clientDTO) {
        ClientEntity client = clientRepository.findByName(name);
        if (client != null) {
            return ResponseEntity.ok().body(toDAO(updateClient(client, clientDTO)));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(StringUtils.CLIENT_NOT_FOUND + name);
    }

    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok().body(clientRepository.findAll().stream()
                .map(this::toDAO).toList());
    }

    public ResponseEntity<Object> get(String name) {
        ClientEntity client = clientRepository.findByName(name);
        if (client != null) {
            return ResponseEntity.ok().body(toDAO(client));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(StringUtils.CLIENT_NOT_FOUND + name);
    }

    public ClientEntity getClient(String name) {
        return clientRepository.findByName(name);
    }

    public ResponseEntity<Object> delete(String name) {
        ClientEntity client = clientRepository.findByName(name);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringUtils.CLIENT_NOT_FOUND + name);
        }
        clientRepository.delete(client);
        return ResponseEntity.ok().body(StringUtils.CLIENT_DELETED);
    }

    private ClientEntity fromDTO(ClientDTO clientDTO) {
        return ClientEntity.builder()
                .name(clientDTO.getName())
                .email(clientDTO.getEmail())
                .phone(clientDTO.getPhone())
                .address(clientDTO.getAddress())
                .zipCode(clientDTO.getZipCode())
                .state(clientDTO.getState())
                .city(clientDTO.getCity())
                .taxId(clientDTO.getTaxId())
                .build();
    }

    private ClientDAO toDAO(ClientEntity client) {
        return ClientDAO.builder()
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .zipCode(client.getZipCode())
                .state(client.getState())
                .city(client.getCity())
                .taxId(client.getTaxId())
                .build();
    }

    private ClientEntity updateClient(ClientEntity existingClient, ClientDTO clientDTO) {
        existingClient.setName(clientDTO.getName());
        existingClient.setEmail(clientDTO.getEmail());
        existingClient.setPhone(clientDTO.getPhone());
        existingClient.setAddress(clientDTO.getAddress());
        existingClient.setTaxId(clientDTO.getTaxId());
        return existingClient;
    }
}
