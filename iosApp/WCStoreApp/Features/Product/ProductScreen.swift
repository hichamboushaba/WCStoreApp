//
//  ProductScreen.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 13/3/2022.
//

import SwiftUI
import WCStoreAppKmm
import KMPNativeCoroutinesCombine

class ProductViewModelProxy: ObservableObject {
    let viewModel:ProductViewModel
    
    @Published private(set) var uiState: ProductViewModel.UiState? = nil
    
    init(productId: Int64) {
        viewModel = KoinKt.get(objCClass: ProductViewModel.self, parameters: [43]) as! ProductViewModel
        createPublisher(for: viewModel.uiStateNative)
            .assertNoFailure()
            .receive(on: RunLoop.main)
            .assign(to: &$uiState)
    }
}

struct ProductScreen: View {
    @ObservedObject private var viewModelProxy: ProductViewModelProxy
    
    init(productId: Int64) {
        self.viewModelProxy = ProductViewModelProxy(productId: productId)
    }
    
    var body: some View {
        if (viewModelProxy.uiState is ProductViewModel.UiStateLoadingState) {
            ProgressView()
        }
        if let state = viewModelProxy.uiState as? ProductViewModel.UiStateSuccessState {
            Text(state.product.name)
                .navigationTitle(state.product.name)
        }
    }
}

struct ProductScreen_Previews: PreviewProvider {
    static var previews: some View {
        ProductScreen(productId: 0)
    }
}
