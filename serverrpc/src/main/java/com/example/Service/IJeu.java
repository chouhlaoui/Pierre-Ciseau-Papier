package com.example.Service;

import java.util.List;

public interface IJeu {

    List<String> playRound(String clientId, String clientChoice);

    String getGameResult(String clientId);

    String getClientHistory(String clientId);

}
