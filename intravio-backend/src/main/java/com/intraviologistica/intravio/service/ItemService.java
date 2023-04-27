package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.model.Item;
import com.intraviologistica.intravio.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void salvarListaDeItens(List<Item> items) {
        itemRepository.saveAll(items);
    }
}
