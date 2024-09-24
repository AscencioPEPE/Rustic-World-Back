package com.rusticworld.app.service;

import com.rusticworld.app.dao.ClientDAO;
import com.rusticworld.app.dao.ClientListPaginatedDAO;
import com.rusticworld.app.dto.ClientDTO;
import com.rusticworld.app.model.ClientEntity;
import com.rusticworld.app.repository.ClientRepository;
import com.rusticworld.app.utils.ClientSpecification;
import com.rusticworld.app.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
            return ResponseEntity.ok().body(toDAO(clientRepository.save(updateClient(client, clientDTO))));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(StringUtils.CLIENT_NOT_FOUND + name);
    }

    public ResponseEntity<Object> getAll(List<String> clientTypes, String sortOrder, Integer limit, Integer page, String namePrefix) {
        Specification<ClientEntity> spec = Specification.where(null);
        if (clientTypes != null && !clientTypes.isEmpty()) {
            spec = spec.and(ClientSpecification.hasClientType(clientTypes));
        }
        if (namePrefix != null && !namePrefix.isEmpty()) {
            spec = spec.and(ClientSpecification.nameStartsWith(namePrefix));
        }
        Sort sort;
        if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by(Sort.Order.desc("name"));
        } else {
            sort = Sort.by(Sort.Order.asc("name"));
        }
        sort = sort.and(Sort.by(Sort.Order.asc("id")));
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        Page<ClientEntity> clientPage = clientRepository.findAll(spec, pageRequest);
        List<ClientDAO> listClientDAO = clientPage.getContent().stream()
                .map(this::toDAO).toList();
        ClientListPaginatedDAO response = ClientListPaginatedDAO.builder()
                .startIndex((int) clientPage.getPageable().getOffset())
                .endIndex((clientPage.getNumberOfElements() > 0)
                        ? (int) (clientPage.getPageable().getOffset() + clientPage.getNumberOfElements() - 1)
                        : (int) clientPage.getPageable().getOffset())
                .count((int) clientPage.getTotalElements())
                .page(clientPage.getNumber())
                .pages(clientPage.getTotalPages())
                .totalClientsPage(clientPage.getNumberOfElements())
                .clients(listClientDAO)
                .build();
        return ResponseEntity.ok(response);
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
                .nameClient(clientDTO.getNameClient())
                .email(clientDTO.getEmail())
                .phone(clientDTO.getPhone())
                .address(clientDTO.getAddress())
                .zipCode(clientDTO.getZipCode())
                .state(clientDTO.getState())
                .city(clientDTO.getCity())
                .clientType(clientDTO.getClientType())
                .taxId(clientDTO.getTaxId())
                .build();
    }

    private ClientDAO toDAO(ClientEntity client) {
        return ClientDAO.builder()
                .name(client.getName())
                .nameClient(client.getNameClient() )
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .zipCode(client.getZipCode())
                .state(client.getState())
                .city(client.getCity())
                .clientType(client.getClientType())
                .taxId(client.getTaxId())
                .build();
    }

    private ClientEntity updateClient(ClientEntity existingClient, ClientDTO clientDTO) {
        existingClient.setName(clientDTO.getName());
        existingClient.setNameClient(clientDTO.getNameClient());
        existingClient.setEmail(clientDTO.getEmail());
        existingClient.setPhone(clientDTO.getPhone());
        existingClient.setAddress(clientDTO.getAddress());
        existingClient.setZipCode(clientDTO.getZipCode());
        existingClient.setState(clientDTO.getState());
        existingClient.setCity(clientDTO.getCity());
        existingClient.setClientType(clientDTO.getClientType());
        existingClient.setTaxId(clientDTO.getTaxId());
        return existingClient;
    }
}
