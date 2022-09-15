//
//  ViewModelProxy.swift
//  WCStoreApp
//
//  Created by Hicham on 14/9/2022.
//

import Foundation
import WCStoreAppKmm
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesCombine
import Combine
import SwiftUI

class ViewModelProxy <ViewModel> : ObservableObject where ViewModel : BaseViewModel {
    let viewModel :ViewModel = Koin.get()
    
    deinit {
        viewModel.close()
    }
}


extension BaseViewModel : ObservableObject {
    
}

extension ViewModelProxy {
    func assignToPublished<Output>(flowProperty: KeyPath<ViewModel, NativeFlow<Output, Error, KotlinUnit>>, value: inout Published<Output>.Publisher, initialValue: Output? = nil) {
        let nativeFlow = viewModel[keyPath: flowProperty]
        let publisher = createPublisher(for: nativeFlow)
        let initialValuePublisher : AnyPublisher<Output, Never>
        if (initialValue != nil) {
            initialValuePublisher = Just(initialValue!).eraseToAnyPublisher()
        } else {
            initialValuePublisher = Empty().eraseToAnyPublisher()
        }
        
        publisher
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
            .assign(to: &value)
    }
}
