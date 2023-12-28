package com.asleepyfish.strategy;

import io.github.asleepyfish.enums.exception.ChatGPTErrorEnum;
import io.github.asleepyfish.exception.ChatGPTException;
import io.github.asleepyfish.strategy.TokenStrategy;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: asleepyfish
 * @Date: 2023/12/28 14:57
 * @Description: SelectSecondStrategy
 */
public class SelectSecondStrategy implements TokenStrategy {

    @Override
    public String getToken(List<String> tokens) {
        if (!CollectionUtils.isEmpty(tokens) && tokens.size() > 1) {
            return tokens.get(1);
        }
        throw new ChatGPTException(ChatGPTErrorEnum.NO_AVAILABLE_TOKEN_ERROR);
    }
}
