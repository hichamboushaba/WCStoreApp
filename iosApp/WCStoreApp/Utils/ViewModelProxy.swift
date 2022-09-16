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
    let viewModel :ViewModel
    
    init (viewModel: ViewModel) {
        self.viewModel = viewModel
    }
    
    init (parameters: [Any]? = nil) {
        self.viewModel = Koin.get(parameters: parameters)
    }
    
    deinit {
        viewModel.close()
    }
}


extension BaseViewModel : ObservableObject {
    
}

extension ViewModelProxy where ViewModel: BaseViewModel {
    func assignToPublished<Output>(from: KeyPath<ViewModel, NativeFlow<Output, Error, KotlinUnit>>, to: inout Published<Output?>.Publisher, initialValue: Output? = nil) {
        createFlowPublisher(from: from, initialValue: initialValue)
            .map { $0 }
            .assign(to: &to)
    }
    
    func assignToPublished<Output>(from: KeyPath<ViewModel, NativeFlow<Output, Error, KotlinUnit>>, to: inout Published<Output>.Publisher, initialValue: Output? = nil) {
        createFlowPublisher(from: from, initialValue: initialValue)
            .assign(to: &to)
    }
    
    private func createFlowPublisher<Output>(from flowProperty: KeyPath<ViewModel, NativeFlow<Output, Error, KotlinUnit>>, initialValue: Output? = nil) -> AnyPublisher<Output, Never> {
        let nativeFlow = viewModel[keyPath: flowProperty]
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
}
