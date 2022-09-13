//
//  FlowWrapper.swift
//  WCStoreApp
//
//  Created by Hicham on 12/9/2022.
//

import SwiftUI
import Combine
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesCombine
import WCStoreAppKmm

@propertyWrapper
public struct FlowWrapper<Value>: DynamicProperty {
    @ObservedObject private var observableFlow: ObservableFlow<Value>
    
    private init(observableFlow: ObservableFlow<Value>) {
        self.observableFlow = observableFlow
    }
    
    init<Failure: Error, Unit>(
        _ nativeFlow: @escaping NativeFlow<Value, Failure, Unit>,
        initialValue: Value
    ) {
        let observableFlow = ObservableFlow(nativeFlow: nativeFlow, initialValue: initialValue)
        self.init(observableFlow: observableFlow)
    }
    
    public var wrappedValue: Value { observableFlow.value }
}

internal class ObservableFlow<Value>: ObservableObject {
    @Published private(set) var value: Value
    
    init<Failure: Error, Unit>(
        nativeFlow: @escaping NativeFlow<Value, Failure, Unit>,
        initialValue: Value
    ) {
        let publisher = createPublisher(for: nativeFlow)
        value = initialValue
        publisher
            .catch { e -> AnyPublisher<Value, Error> in
                // TODO find a better way for ignoring Kotlin's CancellationException here
                if (e.localizedDescription == "Job was cancelled") {
                    return Empty(completeImmediately: false)
                        .eraseToAnyPublisher()
                } else {
                    return Fail(error: e)
                        .eraseToAnyPublisher()
                }
            }
            .assertNoFailure()
            .receive(on: DispatchQueue.main)
            .assign(to: &$value)
        
    }
}
