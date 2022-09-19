//
//  ProductsList.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 10/3/2022.
//

import SwiftUI
import Combine
import WCStoreAppKmm
import KMPNativeCoroutinesCombine

class HomeViewModelWrapper: ViewModelWrapper<HomeViewModel> {
    @Published var productsState: ProductsUiListState = ProductsUiListState(products: [], hasNext: true, state: LoadingState.loading)
    
    init() {
        super.init()
        assignToPublished(from:\.productsNative, to: &$productsState)
    }
}

struct HomeScreen: View {
    @StateObject private var viewModelWrapper = HomeViewModelWrapper()
    
    private var viewModel: HomeViewModel {
        return viewModelWrapper.viewModel
    }
    
    var body: some View {
        Screen(hasNavigationBar: false) {
            ProductsList(
                productsState: viewModelWrapper.productsState,
                onProductClick: viewModel.onProductClicked, loadNext: viewModel.loadNext)
        }
        .effects(viewModel: viewModel)
    }
}
