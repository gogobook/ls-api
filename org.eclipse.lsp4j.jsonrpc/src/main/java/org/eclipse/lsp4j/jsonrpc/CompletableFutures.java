/*******************************************************************************
 * Copyright (c) 2016 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.lsp4j.jsonrpc;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class CompletableFutures {
	
	/**
	 * A utility method to create a {@link CompletableFuture} with cancellation support.
	 * 
	 * @param code a function that accepts a {@link CancelIndicator} and returns the to be computed value
	 * @return a future
	 */
	public static <R> CompletableFuture<R> computeAsync(Function<CancelIndicator, R> code) {
		CompletableFuture<CancelIndicator> start = new CompletableFuture<>();
		CompletableFuture<R> result = start.thenApplyAsync(code);
		CancelIndicator cancelIndicator = () -> {
			if (result.isCancelled()) 
				throw new CancellationException();
		};
		start.complete(cancelIndicator);
		return result;
	}
	
	/**
	 * A utility method to create a {@link CompletableFuture} with cancellation support.
	 * 
	 * @param code a function that accepts a {@link CancelIndicator} and returns the to be computed value
	 * @return a future
	 */
	public static <R> CompletableFuture<R> computeAsync(Executor executor, Function<CancelIndicator, R> code) {
		CompletableFuture<CancelIndicator> start = new CompletableFuture<>();
		CompletableFuture<R> result = start.thenApplyAsync(code, executor);
		CancelIndicator cancelIndicator = () -> {
			if (result.isCancelled()) 
				throw new CancellationException();
		};
		start.complete(cancelIndicator);
		return result;
	}
	
}
