//
//  ViewModelWrapper.swift
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

class ViewModelWrapper <ViewModel> : ObservableObject where ViewModel : BaseViewModel {
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

extension ViewModelWrapper where ViewModel: BaseViewModel {
    func assignToPublished<Output>(from: KeyPath<ViewModel, NativeFlow<Output, Error, KotlinUnit>>, to: inout Published<Output?>.Publisher, initialValue: Output? = nil) {
        createFlowPublisher(from: viewModel[keyPath: from], initialValue: initialValue)
            .map { $0 }
            .assign(to: &to)
    }
    
//    func assignToPublished<Output>(from: KeyPath<ViewModel, NativeFlow<Output, Error, KotlinUnit>>, to: inout Published<Output>.Publisher, initialValue: Output? = nil) {
//        createFlowPublisher(from: viewModel[keyPath: from], initialValue: initialValue)
//            .assign(to: &to)
//    }
}
