//
//  ProductsList.swift
//  WCStoreApp
//
//  Created by Hicham on 13/9/2022.
//

import SwiftUI
import WCStoreAppKmm

struct ProductsList: View {
    let productsState: ProductsUiListState
    let onProductClick: (Product) -> Void
    let loadNext: () -> Void
    
    var body: some View {
        List {
            ForEach(productsState.products, id: \.product.id) { productUiModel in
                ProductsListRowView(uiModel: productUiModel, onClick: {
                    onProductClick(productUiModel.product)
                })
                .buttonStyle(BorderlessButtonStyle())
            }
            if productsState.hasNext {
                nextPageView
            }
        }
    }
    
    private var nextPageView: some View {
        HStack {
            Spacer()
            VStack {
                ProgressView()
            }
            Spacer()
        }
        .onAppear(perform: {
            loadNext()
        })
    }
}

private struct ProductsListRowView: View {
    let uiModel: ProductUiModel
    let onClick: () -> Void
    
    var body: some View {
        HStack {
            if let image = uiModel.product.images.first,
               let url = URL(string: image) {
                AsyncImage(url: url) { image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }
                .frame(width: 50, height: 50)
                .clipShape(RoundedRectangle(cornerRadius: 8))
            } else {
                RoundedRectangle(cornerRadius: 8)
                    .frame(width: 50, height: 50)
                    .foregroundColor(.gray)
            }
            VStack(alignment: .leading) {
                Text(uiModel.product.name )
                    .font(.title3)
                    .foregroundColor(.accentColor)
                Text(uiModel.priceFormatted)
                    .font(.footnote)
                    .foregroundColor(.gray)
            }
            .frame(maxWidth:.infinity, alignment: .leading)
            
            CartStepper(
                count: Int(uiModel.quantityInCart),
                addClick: {
                    uiModel.addToCart()
                },
                deleteClick: {
                    uiModel.deleteFromCart()
                }
            )
        }
        .onTapGesture(perform: onClick)
    }
}
