//
// Created by Hicham on 7/12/2022.
//

import Foundation
import Combine
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesCombine
import WCStoreAppKmm


func createFlowPublisher<Output>(from nativeFlow: @escaping NativeFlow<Output, Error, KotlinUnit>, initialValue: Output? = nil) -> AnyPublisher<Output, Never> {
    let publisher = createPublisher(for: nativeFlow)
    let initialValuePublisher : AnyPublisher<Output, Never>
    if (initialValue != nil) {
        initialValuePublisher = Just(initialValue!).eraseToAnyPublisher()
    } else {
        initialValuePublisher = Empty().eraseToAnyPublisher()
    }

    return publisher
            .catch { e -> AnyPublisher<Output, Error> in
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
            .merge(with: initialValuePublisher)
            .receive(on: DispatchQueue.main)
            .eraseToAnyPublisher()
}